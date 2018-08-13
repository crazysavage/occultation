package com.occultation.www.net;

import com.alibaba.fastjson.JSON;
import com.occultation.www.annotation.Fetch;
import com.occultation.www.enums.HttpMethodEnum;
import com.occultation.www.expections.NoSuchMethodTypeException;
import com.occultation.www.util.Assert;
import com.occultation.www.util.UrlUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-22 17:24
 */
@Fetch("default")
public class DefaultFetch extends AbstractFetch {
    
    private static final int TIME_OUT = 10000;
    
    private static final Logger log = LoggerFactory.getLogger(DefaultFetch.class);
    
    private PoolingHttpClientConnectionManager cm;
    
    private final HttpClientContext cookieContext;

    private final CloseableHttpClient client;

    public DefaultFetch() {
        super();
        this.cookieContext = HttpClientContext.create();
        this.cookieContext.setCookieStore(new BasicCookieStore());
        this.client = initClient();
    }

    @Override
    public void doFetch(SpiderRequest req, SpiderResponse result) {
        HttpRequestBase httpReq = toHttpReq(req) ;
        for (Map.Entry<String, String> entry : req.getCookies().entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(entry.getKey(), entry.getValue());
            cookie.setPath("/");
            cookie.setDomain(httpReq.getURI().getHost());
            cookieContext.getCookieStore().addCookie(cookie);
        }
        execute(httpReq,1,result);
    }

    private void execute(HttpRequestBase req, int redirectNum, SpiderResponse result) {
        try {
            HttpResponse response = client.execute(req,cookieContext);
            int status  = response.getStatusLine().getStatusCode();
            result.setStatus(status);
            if (status > 199 && status <300) {
                result.setStatus(200);

                if (response.getEntity() == null) {
                    return;
                }

                String contentType = response.getEntity().getContentType().getValue();
                String charset = getCharset(contentType);
                result.setContentType(contentType);
                result.setCharset(charset);

                String content;

                if (isImage(contentType)) {
                    content = new BASE64Encoder().encode(EntityUtils.toByteArray(response.getEntity()));
                } else {
                    content = EntityUtils.toString(response.getEntity(),charset);
                }
                result.setContent(content);
            } else if ((status == 301 || status == 302) && redirectNum < 4) {
                //重定向
                String redirectUrl = response.getFirstHeader("Location").getValue();
                try {
                    req.setURI(new URI(redirectUrl));
                } catch (URISyntaxException e) {
                    throw new RuntimeException("redirect url syntax is error, url = " + redirectUrl ,e);
                }

                execute(req,++redirectNum,result);
            } else {
                //异常访问4XX 5XX
                log.error("http stauts : {}, url : {}",status,req.getURI().toString());
            }

        } catch (IOException e) {

            log.error("connect error",e);

        } finally {
            req.releaseConnection();
        }
    }

    private CloseableHttpClient initClient() {
        return HttpClients.custom()
                .setConnectionManager(getConnectionManager())
                .build();
    }
    
    private PoolingHttpClientConnectionManager  getConnectionManager() {
        if (this.cm == null) {
            initConnectionManager();
        }
        return this.cm;

    }
    
    private void initConnectionManager() {

        Registry<ConnectionSocketFactory> registry = null;
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
            SSLConnectionSocketFactory sslf = new SSLConnectionSocketFactory(sslContext);
            registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslf)
                    .build();
        } catch (Exception e) {
            log.error("certificate problems",e);
        }
        if (registry == null) {
            this.cm = new PoolingHttpClientConnectionManager();
        } else {
            this.cm = new PoolingHttpClientConnectionManager(registry);
        }
        this.cm.setMaxTotal(200);
    }
    
    private HttpRequestBase toHttpReq(SpiderRequest req) {
        HttpMethodEnum method = req.getType();
        Assert.isTrue(method != null, "http method is undefined"); 
        switch (method) {
        case GET:
            String url = req.getUrl();
            String paramStr = req.paramToString();
            if (StringUtils.isNotEmpty(paramStr)) {
                url += url.contains("?") ? "&" : "?";
                url += paramStr;
            }
            HttpGet get = new HttpGet(url);
            wrapRequest(get,req);
            return get;
        case POST:
            HttpPost post = new HttpPost(req.getUrl());
            try {
                post.setEntity(new StringEntity(JSON.toJSONString(req.getParams())));
            } catch (UnsupportedEncodingException e) {
                log.error("entity encoding error,url:{}",req.getUrl(),e);
            }
            wrapRequest(post,req);
            return post;
        default:
            throw new NoSuchMethodTypeException(method + " type is undefined");
        }
    }
    
    private void wrapRequest(HttpRequestBase request,SpiderRequest req) {
        //设置agent
        request.setHeader("User-Agent",UserAgent.getAgent());
        //设置head
        for(Map.Entry<String, String> entry : req.getHeads().entrySet()) {
            request.setHeader(entry.getKey(), entry.getValue());
        }

        RequestConfig.Builder bulider = RequestConfig.custom()
        .setConnectionRequestTimeout(TIME_OUT)
        .setSocketTimeout(TIME_OUT)
        .setConnectTimeout(TIME_OUT)
        .setRedirectsEnabled(false);

        if (req.getProxy() != null) {
            bulider.setProxy(HttpHost.create(req.getProxy().toURI()));
        }


        request.setConfig(bulider.build());    
    }
    
    private String getCharset(String content) {
        if (StringUtils.isNotEmpty(content)) {
            Pattern p = Pattern.compile("(?<=charset=).*");
            Matcher m = p.matcher(content);
            if (m.find()) {
                return m.group();
            }
        }
        return "UTF-8";
    }
    
    private boolean isImage(String content) {
        return content != null && content.toLowerCase().startsWith("image");
    }
    
}
