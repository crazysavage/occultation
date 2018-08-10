package com.occultation.www.kuaiyishou;

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
public class FileDownLoad {
    private static PoolingHttpClientConnectionManager cm;

    public static final int cache = 10 * 1024;

    public static final String FILE_DIR = "/videoShort";

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
    public static String download(String url) {
        String filepath = null;
        InputStream is = null;
        FileOutputStream fileout = null;
        try {
            HttpClient client = getClient();
            HttpGet httpget = new HttpGet(url);
            httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
            httpget.setHeader("Accept","*/*;q=0.8");
            httpget.setHeader("Accept-Encoding","gzip, deflate");

            HttpResponse response = client.execute(httpget);

            int status = response.getStatusLine().getStatusCode();

            if (status > 299 && status < 400) {
                String redirectUrl = response.getFirstHeader("Location").getValue();
                String direct =  UrlUtils.getAbsoluteUrl(url, redirectUrl);
                return download(direct);
            }

            if (status > 199 && status < 300) {
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                String contentType = entity.getContentType().getValue();
                filepath = getFilePath(contentType);
                File file = new File(filepath);
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

    public static String getFilePath(String contentType) {
        String filename = System.currentTimeMillis() + "";

        return FILE_DIR + "/" + filename + "." + getSuffix(contentType);
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
        String url = UrlEncrypt.encrypt("http://api.amemv.com/aweme/v1/play/?video_id=:183:96:127:154:138:204:133:139:137:96:126:116:163:150:133:156:177:163:126:218:205:209:177:188:187:160:179:116:115:102:180:154&line=0&ratio=720p&media_type=4&vr_type=0&test_cdn=None&improve_bitrate=0");
        FileDownLoad.download(url);
    }

}
