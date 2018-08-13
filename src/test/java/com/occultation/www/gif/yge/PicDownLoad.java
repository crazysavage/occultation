package com.occultation.www.gif.yge;

import com.occultation.www.kuaiyishou.UrlEncrypt;
import com.occultation.www.util.UrlUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-08 10:42
 */
public class PicDownLoad {

    private static PoolingHttpClientConnectionManager cm;

    public static final int cache = 10 * 1024;

    public static final String FILE_DIR = "/gif";

    static {
        File dir = new File(FILE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * 根据url下载文件，文件名从response header头中获取
     * @param url
     * @return
     */
    public static String download(String url,String name) {
        String filepath = null;
        InputStream is = null;
        FileOutputStream fileout = null;
        try {
            HttpClient client = getClient();
            HttpGet httpget = new HttpGet(url);

            HttpResponse response = client.execute(httpget);

            int status = response.getStatusLine().getStatusCode();

            if (status > 299 && status < 400) {
                String redirectUrl = response.getFirstHeader("Location").getValue();
                String direct =  UrlUtils.getAbsoluteUrl(url, redirectUrl);
                return download(direct,name);
            }

            if (status > 199 && status < 300) {
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                String contentType = entity.getContentType().getValue();
                File file = getFilePath(name,contentType);

                fileout = new FileOutputStream(file);
                byte[] buffer=new byte[cache];
                int ch;
                while ((ch = is.read(buffer)) != -1) {
                    fileout.write(buffer,0,ch);
                }
                fileout.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fileout != null) {
                    fileout.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filepath;
    }

    public static File getFilePath(String filename , String contentType) {

        return getFilePath(filename,contentType,0);
    }

    public static File getFilePath(String filename , String contentType,int count) {
        String filePath = FILE_DIR + "/" + filename + (count == 0 ? "" : count) + "." + getSuffix(contentType);
        File f = new File(filePath);
        if (f.exists()) {
            f = getFilePath(filename,contentType,++count);
        }

        return f;
    }

    private static CloseableHttpClient getClient() {
        return HttpClients.custom()
                .setConnectionManager(getConnectionManager())
                .build();
    }

    private static PoolingHttpClientConnectionManager getConnectionManager() {
        if (cm == null) {
            initConnectionManager();
        }
        return cm;

    }

    private static void initConnectionManager() {

        Registry<ConnectionSocketFactory> registry = null;
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
            SSLConnectionSocketFactory sslf = new SSLConnectionSocketFactory(sslContext);
            registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslf)
                    .build();
        } catch (Exception ignored) {

        }
        if (registry == null) {
            cm = new PoolingHttpClientConnectionManager();
        } else {
            cm = new PoolingHttpClientConnectionManager(registry);
        }
    }

    private static String getSuffix(String contentType) {
        return contentType.replaceFirst("[a-z]+/","");
    }

    public static void main(String[] args) {
        String url = UrlEncrypt.encrypt("http://ww1.sinaimg.cn/large/b6041b81gy1fsbn20deiwg20dw07te81.jpg");
        PicDownLoad.download(url,"IPZ-643");
    }

}
