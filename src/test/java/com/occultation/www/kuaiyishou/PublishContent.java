package com.occultation.www.kuaiyishou;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.occultation.www.dazong.FileInfo;
import com.occultation.www.dazong.ShopInfo;
import com.occultation.www.mafengwo.SqlHelp;
import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 发布请求
 *
 * @author yejy
 * @since 2018-09-04 23:22
 */
public class PublishContent implements Runnable {


    private static final Logger log = LoggerFactory.getLogger(PublishContent.class);

    private static final CloseableHttpClient client;

    private static final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10,50,60, TimeUnit.SECONDS,new LinkedBlockingQueue<>());

    private Video v;

    private ShopInfo s;

    static {
        client = initClient();
    }

    public PublishContent(Video v) {
        this.v = v;
    }

    public PublishContent(ShopInfo s) {
        this.s = s;
    }

    public static void main(String[] args) {

        String sql = "SELECT * FROM shop  LIMIT 100 , 150";
        List<ShopInfo> shop = SqlHelp.getInstance().select(sql,ShopInfo.class);
        shop.forEach(s -> threadPool.submit(new PublishContent(s)));


        String sql2 = "SELECT * FROM short_video WHERE height != 0 LIMIT 100 , 150";
        List<Video> videos = SqlHelp.getInstance().select(sql2,Video.class);
        videos.forEach(v -> threadPool.submit(new PublishContent(v)));


        threadPool.shutdown();
        while (true) {
            if (threadPool.isTerminated()) {
                log.info("执行结束");
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try {
            if (v != null) {
                publish_mng(v);
            }

            if (s!=null) {
                publish_mng(s);
            }

        }catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        log.info("耗时:{}ms",System.currentTimeMillis() - start);
    }




    private static final String PUBLISH_MNG_URL = "http://devcenter.caocaokeji.cn"
        + "/go-cntmng/content/add";


    private void publish_mng(ShopInfo shop) {
        Map<String,Object> params = new HashMap<>();
        try {
            params.put("fileIds",Collections.singleton(getFileId(JSONObject.parseObject(shop.getFile(),FileInfo.class))));
        } catch (Exception e) {
            return;
        }
        params.put("accountId", 10001);
        params.put("address", shop.getLocation());
        params.put("contentType", 3);
        params.put("description", shop.getDesc() + "\n" + "类型：" + shop.getTag());
        params.put("latitude", Double.valueOf(shop.getLat()));
        params.put("longitude", Double.valueOf(shop.getLng()));
        params.put("operateNo", getOperationId());
        params.put("recommendLevel", 5);
        params.put("tags", Collections.singleton("2"));
        params.put("title", shop.getTitle());

        Map<String,String> heads = new HashMap<>();
        heads.put("Content-Type","application/json");
        heads.put("Cookie", "UM_distinctid=163aaf1c6fb8b1-0fa08ebf47133c-393d5c04-100200-163aaf1c6fc7db; COOKIE_AUTH_TICKET=7ffdf73c3b1b46e39f9822c8552044dd");
        String res = httpExecute(PUBLISH_MNG_URL,params,heads);
        if (JSONPath.read(res,"$.code").equals(0)) {
            log.info("发布成功,code：{},title：{}", JSONPath.read(res,"$.data.contentCode"), shop.getTitle());
        } else {
            log.error("发布失败, {}", res);
        }

    }

    private void publish_mng(Video video) {

        Map<String,Object> params = new HashMap<>();
        params.put("accountId", 10001);
        params.put("address", "吉利大厦");
        params.put("contentType", 1);
        params.put("description", "该数据来自爬虫");
        params.put("fileIds", Collections.singleton(getFileId(v)));
        params.put("latitude", 30.208883);
        params.put("longitude", 120.218818);
        params.put("operateNo", getOperationId());
        params.put("recommendLevel", 5);
        params.put("tags", Collections.singleton("2"));
        params.put("title", video.getDesc());

        Map<String,String> heads = new HashMap<>();
        heads.put("Content-Type","application/json");
        heads.put("Cookie", "UM_distinctid=163aaf1c6fb8b1-0fa08ebf47133c-393d5c04-100200-163aaf1c6fc7db; COOKIE_AUTH_TICKET=7ffdf73c3b1b46e39f9822c8552044dd");
        String res = httpExecute(PUBLISH_MNG_URL,params,heads);
        if (JSONPath.read(res,"$.code").equals(0)) {
            log.info("发布成功,code：{},title：{}", JSONPath.read(res,"$.data.contentCode"), video.getDesc());
        } else {
            log.error("发布失败, {}", res);
        }
    }

    private static final String PUBLISH_URL = "https://devcap.caocaokeji.cn/content/publish/1.0"
        + "?clientType=3&version=1.00&appVersion=2.5.0&biz=9&appCode=no_sign&key=58eedbce80619f65563a1969&cityCode=330100&token=CEiEwTED8AWLCU24&uid=272142&timestamp=1535528404051&sign=0811D59B3017BFDA1700129E7126262E";


    private void publish(ShopInfo shop) {

        Map<String,Object> params = new HashMap<>();
        params.put("contentType",3);
        params.put("title",shop.getTitle());
        params.put("contentDesc",shop.getDesc() + "\n" + "类型：" + shop.getTag());
        params.put("poi","{\"latitude\":" + shop.getLat() + ",\"locationName\":\"" + shop.getLocation() + "\",\"longitude\":" + shop.getLng() + "}");

        params.put("tagCodes",2);
        try {
            params.put("fileIds",getFileId(JSONObject.parseObject(shop.getFile(),FileInfo.class)));
        } catch (Exception e) {
            return;
        }
        params.put("operateNo",getOperationId());

        String res = httpExecute(PUBLISH_URL,params,null);
        if (JSONPath.read(res,"$.code").equals(0)) {
            log.info("发布成功,code：{},title：{}", JSONPath.read(res,"$.data.contentCode"), shop.getTitle());
        } else {
            log.error("发布失败, {}", res);
        }
    }

    /*获取文件id*/
    private String getFileId(FileInfo fileInfo) {
        Map<String,Object> params = new HashMap<>();
        params.put("fileName",fileInfo.getPath().replace("\\dazong\\",""));
        params.put("height",fileInfo.getHeight());
        params.put("width",fileInfo.getWidth());
        params.put("md5",fileInfo.getSignature());
        params.put("title","测试img");

        String res = httpExecute(FILE_URL,params,null);
        if (JSONPath.read(res,"$.data.resultCode").equals(2001)) {
            return JSONPath.read(res,"$.data.fileId").toString();
        }
        Map<String,Object> httpInfo = JSONObject.parseObject(JSONPath.read(res,"$.data.httpInfo").toString(),Map.class);
        upload(httpInfo.get("url").toString(),(Map<String,String>)httpInfo.get("headers"),(Map<String,String>)httpInfo.get("params"),new File(fileInfo.getPath()));
        return JSONPath.read(res,"$.data.fileId").toString();
    }


    /**
     * 发布
     */
    private void publish(Video video) {

        Map<String,Object> params = new HashMap<>();
        params.put("contentType",1);
        params.put("title",video.getDesc());
        params.put("contentDesc","该数据来自于爬虫");
        params.put("poi","{\"latitude\":30.208883,\"locationName\":\"吉利大厦\",\"longitude\":120.218818}");
        params.put("tagCodes",2);
        params.put("fileIds",getFileId(video));
        params.put("operateNo",getOperationId());

        String res = httpExecute(PUBLISH_URL,params,null);
        if (JSONPath.read(res,"$.code").equals(0)) {
            log.info("发布成功,code：{},title：{}", JSONPath.read(res,"$.data.contentCode"), video.getDesc());
        } else {
            log.error("发布失败, {}", res);
        }


    }

    private static final String OPERATION_ID = "http://devcap.caocaokeji.cn/content/getOperateId/1.0"
            + "?clientType=3&version=1.00&appVersion=2.5.0&biz=9&appCode=no_sign&key=58eedbce80619f65563a1969&cityCode=330100&token=CEiEwTED8AWLCU24&uid=272142&timestamp=1535528404051&sign=0811D59B3017BFDA1700129E7126262E";

    /*获取流水号*/
    private String getOperationId() {
        String result = httpExecute(OPERATION_ID,null,null);

        if (Integer.valueOf(JSONPath.read(result,"$.code").toString()).equals(0)) {
            return JSONPath.read(result,"$.data.operateNo").toString();
        }
        throw new RuntimeException("请求失败");
    }


    private static final String FILE_URL = "http://devcap.caocaokeji.cn/content/uploadUrl/1.0"
            + "?clientType=3&version=1.00&appVersion=2.5.0&biz=9&appCode=no_sign&key=58eedbce80619f65563a1969&cityCode=330100&token=CEiEwTED8AWLCU24&uid=272142&timestamp=1535528404051&sign=0811D59B3017BFDA1700129E7126262E";

    /*获取文件id*/
    private String getFileId(Video video) {
        Map<String,Object> params = new HashMap<>();
        params.put("fileName",video.getVideo_url().replace("/videoShort/",""));
        params.put("height",video.getHeight());
        params.put("width",video.getWidth());
        params.put("md5",video.getSignature());
        params.put("title","测试");

        String res = httpExecute(FILE_URL,params,null);
        if (JSONPath.read(res,"$.data.resultCode").equals(2001)) {
            return JSONPath.read(res,"$.data.fileId").toString();
        }

        Map<String,Object> httpInfo = JSONObject.parseObject(JSONPath.read(res,"$.data.httpInfo").toString(),Map.class);

        upload(httpInfo.get("url").toString(),(Map<String,String>)httpInfo.get("headers"),(Map<String,String>)httpInfo.get("params"),new File(video.getVideo_url()));
        return JSONPath.read(res,"$.data.fileId").toString();
    }


    private String httpExecute(String url, Map<String,Object> params, Map<String,String> heads) {
        HttpPost post = new HttpPost(url);
        RequestConfig.Builder bulider = RequestConfig.custom()
            .setConnectionRequestTimeout(30000)
            .setSocketTimeout(30000)
            .setConnectTimeout(30000)
            .setRedirectsEnabled(false);
        post.setConfig(bulider.build());


        if (MapUtils.isNotEmpty(heads)) {
            heads.forEach(post::addHeader);
        }


        if (MapUtils.isNotEmpty(params)) {

            if (heads!=null) {


                StringEntity entity = new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON );
                post.setEntity(entity);

            } else {

                List<BasicNameValuePair> nv = params.entrySet().stream()
                    .map(e -> new BasicNameValuePair(e.getKey(), e.getValue().toString())).collect(Collectors.toList());

                try {
                    post.setEntity(new UrlEncodedFormEntity(nv, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    log.error("模型注入失败", e);
                    return null;
                }
            }
        }



        try {
            HttpResponse response = client.execute(post);
            int code = response.getStatusLine().getStatusCode();
            if (code > 199 && code < 300) {
                if (response.getEntity() != null) {
                    return EntityUtils.toString(response.getEntity(), "UTF-8");
                }
            } else {
                throw new RuntimeException("请求失败，状态码：" + code);
            }
            throw new RuntimeException("当前请求无返回值");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            post.releaseConnection();
        }

    }

    private void upload(String url,Map<String,String> heads,Map<String,String> params, File f) {

        if (MapUtils.isNotEmpty(params)) {
            StringBuilder sb = new StringBuilder(url);
            sb.append("?");
            params.forEach((k,v) -> {
                sb.append(k);
                sb.append("=");
                sb.append("v");
                sb.append("&");
            });
            url = sb.substring(0,sb.length()-1);
        }

        HttpPut request = new HttpPut(url);
        if (MapUtils.isNotEmpty(heads)) {
            heads.forEach(request::addHeader);
        }
        request.setEntity(new FileEntity(f));
        RequestConfig.Builder bulider = RequestConfig.custom()
                .setConnectionRequestTimeout(300000)
                .setSocketTimeout(300000)
                .setConnectTimeout(300000)
                .setRedirectsEnabled(false);
        request.setConfig(bulider.build());


        try {
            HttpResponse response = client.execute(request);
            int code = response.getStatusLine().getStatusCode();
            if (code > 199 && code < 300) {
                log.info("上传文件[{}]成功",f.getName());
            } else {
                throw new RuntimeException("请求失败，状态码：" + code);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            request.releaseConnection();
        }


    }

    private static CloseableHttpClient initClient() {
        return HttpClients.custom()
            .setConnectionManager(initConnectionManager())
            .build();
    }

    private static PoolingHttpClientConnectionManager initConnectionManager() {

        Registry<ConnectionSocketFactory> registry = null;
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
            SSLConnectionSocketFactory sslf = new SSLConnectionSocketFactory(sslContext);
            registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslf)
                .build();
        } catch (Exception ignore) {
        }
        PoolingHttpClientConnectionManager cm;
        if (registry == null) {
            cm = new PoolingHttpClientConnectionManager();
        } else {
            cm = new PoolingHttpClientConnectionManager(registry);
        }
        cm.setMaxTotal(200);
        return cm;
    }


}
