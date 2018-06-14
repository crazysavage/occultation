package com.occultation.www.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Encoder;

/**
 * @Type HttpTest
 * @Desc TODO
 * @author Savage
 * @created 2017年8月9日 下午4:07:28
 * @version 1.0.0
 */
public class HttpTest {
    private static final Logger log = LoggerFactory.getLogger(HttpTest.class);
    
    public static void main(String[] args) {
        
        getCostTime(() -> {
            testHttp();
            return null;
        },"httpClient");

    }



    
    public static void testImg() {
        CloseableHttpClient x = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://fs.static.guahao-inc.com/sro26455468.jpg?token=cfb2855392761cb6a39cecbc4f51d5c9");
        try {
            HttpResponse response = x.execute(get);
            System.out.println(response.getStatusLine().toString());
            String contentType = response.getEntity().getContentType().getValue();
            System.out.println(contentType);
            Pattern p = Pattern.compile("(?<=charset=).*");
            Matcher m = p.matcher(contentType);
            if (m.find()) {
                System.out.println(m.group(0));
            }
            new BASE64Encoder().encode(EntityUtils.toByteArray(response.getEntity()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error("发生异常.", e);
        }
        
    }
    
    public static void testHttp() {
        CloseableHttpClient x = HttpClients.createDefault();


        HttpUriRequest post = getReq();
         
        try {
            HttpResponse response = x.execute(post);
            System.out.println(response.getStatusLine().toString());
            String contentType = response.getEntity().getContentType().getValue();
            System.out.println(contentType);
            Pattern p = Pattern.compile("(?<=charset=).*");
            Matcher m = p.matcher(contentType);
            if (m.find()) {
                System.out.println(m.group(0));
            }
            
            System.out.println("*************************");
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static HttpUriRequest getReq() {
        HttpPost post = new HttpPost("http://sns-web.guahao-inc.com/help/answer/list.json");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept", "application/json");
        try {
            post.setEntity(new StringEntity("{\"topicId\":\"OTWcG425243\",\"pageIndex\":1,\"pageSize\":10}"));
        } catch (UnsupportedEncodingException e) {

        }
        return post;
    }
    
    /**
    *
    * @param call
    */
   private static <V> void getCostTime(Callable<V> call,String name) {
       try {
           Long startTime = System.currentTimeMillis();
           call.call();
           Long endTime = System.currentTimeMillis();
           System.err.println("************************from "+ name +"***************************");
           System.out.println("cost time : " + (endTime - startTime));
           System.err.println("***************************************************");
       } catch (Exception e) {
           e.printStackTrace();
       }
       
   }

}
