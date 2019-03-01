package com.occultation.www.dazong;

import com.occultation.www.util.UrlUtils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
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

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-08 10:42
 */
public class HttpExecute {

    private static PoolingHttpClientConnectionManager cm;

    public static final int cache = 10 * 1024;

    public static final String FILE_DIR = "/dazong";

    private static final CloseableHttpClient client;


    static {
        client = initClient();

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
    public static FileInfo download(String url,String name) {
        HttpGet httpget = null;
        try {
            httpget = new HttpGet(url);

            RequestConfig.Builder bulider = RequestConfig.custom()
                    .setConnectionRequestTimeout(3000)
                    .setSocketTimeout(3000)
                    .setConnectTimeout(3000)
                    .setRedirectsEnabled(false);
            httpget.setConfig(bulider.build());



            HttpResponse response = client.execute(httpget);

            int status = response.getStatusLine().getStatusCode();

            if (status > 299 && status < 400) {
                String redirectUrl = response.getFirstHeader("Location").getValue();
                String direct =  UrlUtils.getAbsoluteUrl(url, redirectUrl);
                return download(direct,name);
            }

            if (status > 199 && status < 300) {
                return getFileInfo(response.getEntity(),url,name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpget != null) {
                httpget.releaseConnection();
            }
        }
        return null;
    }

    private static File getFilePath(String filename , String suffix) {

        return getFilePath(filename,suffix,0);
    }

    private static File getFilePath(String filename , String suffix,int count) {
        String filePath = FILE_DIR + "/" + filename + (count == 0 ? "" : count) + "." + suffix;
        File f = new File(filePath);
        if (f.exists()) {
            f = getFilePath(filename,suffix,++count);
        }

        return f;
    }

    private static FileInfo getFileInfo(HttpEntity entity,String url,String name) {

        FileInfo fileInfo = new FileInfo();
        fileInfo.setUrl(url);
        InputStream is = null;
        FileOutputStream fileout = null;
        try {
            BufferedImage image = ImageIO.read(entity.getContent());
            fileInfo.setHeight(image.getHeight());
            fileInfo.setWidth(image.getWidth());

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            String contentType = entity.getContentType().getValue();
            String suffix = getSuffix(contentType);
            ImageIO.write(image, suffix, os);
            fileInfo.setSignature(getMD5(os.toByteArray()));
            File file = getFilePath(name,suffix);
            fileInfo.setPath(file.getPath());

            is = new ByteArrayInputStream(os.toByteArray());
            fileout = new FileOutputStream(file);
            byte[] buffer=new byte[cache];
            int ch;
            while ((ch = is.read(buffer)) != -1) {
                fileout.write(buffer,0,ch);
            }
            fileout.flush();
            return fileInfo;

        } catch (IOException e) {
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

        return null;


    }

    private static String getMD5(byte[] buffer) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buffer);
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return null;
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

    private static String getSuffix(String contentType) {
        return contentType.replaceFirst("[a-z]+/","");
    }

    public static void main(String[] args) {
        FileInfo fileInfo = HttpExecute.download("http://p1.meituan.net/mogu/772579dc3349a1a362224e602973f0ab564331.jpg%40340w_255h_1e_1c_1l%7Cwatermark%3D1%26%26r%3D1%26p%3D9%26x%3D2%26y%3D2%26relative%3D1%26o%3D20",
            "test");
        System.out.println(fileInfo.toString());

    }

}
