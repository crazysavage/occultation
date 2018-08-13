package com.occultation.www.net;

import com.occultation.www.enums.HttpMethodEnum;
import com.occultation.www.net.proxy.HttpProxy;
import com.occultation.www.util.Assert;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Type SpiderRequest
 * @Desc TODO
 * @author liss
 * @created 2017年6月30日 下午3:28:27
 * @version 1.0.0
 */
public class SpiderRequest implements Serializable {

    private static final long serialVersionUID = 3797374622690864165L;

    private String key;
    private String url;
    private HttpMethodEnum type;
    private HttpProxy proxy;
    private Map<String,String> params;
    private Map<String,String> heads;
    private Map<String,String> cookies;
    
    public SpiderRequest() {
        this.params = new HashMap<>();
        this.heads = new HashMap<>();
        this.cookies = new HashMap<>();
    }
    
    public SpiderRequest(String url) {
        this();
        this.url = url;
        this.type = HttpMethodEnum.GET;
    }

    public SpiderRequest(String url,HttpMethodEnum type) {
        this();
        this.url = url;
        this.type = type;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.key = null;
    }

    public HttpMethodEnum getType() {
        return type;
    }

    public void setType(HttpMethodEnum type) {
        this.type = type;
    }

    public SpiderRequest addParam(String key,String val) {
        this.params.put(key, val);
        return this;
    }
    
    public SpiderRequest addParams(Map<String,String> params) {
        this.params.putAll(params);
        return this;
    }
    
    public String delParam(String key) {
        return this.params.remove(key);
    }
    
    public void clearParms() {
        this.params.clear();
    }

    public String getParam(String name) {
        return this.params.get(name);
    }
    
    public SpiderRequest addHead(String name,String val) {
        this.heads.put(name, val);
        return this;
    }
    
    public SpiderRequest addHeads(Map<String,String> heads) {
        this.heads.putAll(heads);
        return this;
    }
    
    public String delHead(String key) {
        return this.heads.remove(key);
    }
    
    public void clearHeads() {
        this.heads.clear();
    }

    public String getHead(String name) {
        return this.heads.get(name);
    }

    public void setRefer(String url) {
        addHead("Referer",url);
    }
    
    public SpiderRequest addCookie(String name,String cookie) {
        this.cookies.put(name, cookie);
        return this;
    }
    
    public SpiderRequest setCookies(Map<String,String> cookies) {
        this.cookies.putAll(cookies);
        return this;
    }
    
    public String delCookie(String key) {
        return this.cookies.remove(key);
    }
    
    public void clearCookies() {
        this.cookies.clear();
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, String> getHeads() {
        return heads;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }
    
    public String getKey() {

        if (StringUtils.isNotEmpty(this.key)) {
            return this.key;
        }


        Assert.isTrue(StringUtils.isNoneEmpty(this.url), "请求中的Url为空");
        String s = "";
        String keyStr = this.url.replaceAll("https?://","") + params.toString();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(keyStr.getBytes());
            byte[] temp = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte t: temp) {
                int value = t & 0xff;
                if(value < 16) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(value));
            }
            s = sb.toString();
            this.key = s;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
        
    }

    public SpiderRequest subRequest(String url) {
        SpiderRequest sub = null;
        try {
            sub = (SpiderRequest) copy();
            sub.setUrl(url);
            sub.setRefer(this.url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sub;
    }

    public Object copy() throws IOException, ClassNotFoundException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        return ois.readObject();
    }

    public String paramToString() {
        if (params.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(64);
        for (Map.Entry param : params.entrySet()) {
            sb.append(param.getKey());
            sb.append("=");
            sb.append(param.getValue());
            sb.append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public HttpProxy getProxy() {
        return proxy;
    }

    public void setProxy(HttpProxy proxy) {
        this.proxy = proxy;
    }
}
