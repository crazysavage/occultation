package com.occultation.www.util;

import com.occultation.www.net.SpiderRequest;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;


/**
 * @Type UrlUtil
 * @Desc TODO
 * @author Savage
 * @created 2017年8月21日 下午3:44:22
 * @version 1.0.0
 */
public class UrlUtils {

    private static final Pattern PLACEHOLDER = Pattern.compile("\\{(.*?)}");

    private static final String IF_NOT_EXIST_R_NULL = "!";

    public static String composeUrl(String url,SpiderRequest req,Object bean) {

        boolean RETURN_NULL =  url.startsWith(IF_NOT_EXIST_R_NULL);
        String urlValue = RETURN_NULL ? url.substring(IF_NOT_EXIST_R_NULL.length()) : url;


        Matcher matcher = PLACEHOLDER.matcher(urlValue);
        List<String> names = new ArrayList<>();
        while(matcher.find()) {
            String name = matcher.group(1);
            names.add(name);
        }
        for (String name : names) {
            Object val = req.getParam(name);
            if (val == null) {
                val = BeanUtils.getValue(bean,name);
            }
            if (val == null) {

                if (RETURN_NULL) {
                    return "";
                }

                val = "";
            }
            urlValue = urlValue.replaceFirst("\\{(.*?)}",getURLEncoderString(val.toString()));
        }

        return urlValue;
    }

    public static Map<String, String> match(String url, String regex) {
        String regexSrc = StringUtils.replace(regex, "?", "\\?");
        StringBuffer sb = new StringBuffer();
        Matcher matcher = PLACEHOLDER.matcher(regexSrc);
        List<String> names = new ArrayList<>();
        while(matcher.find()) {
            matcher.appendReplacement(sb, "([^/]*)");
            String name = matcher.group(1);
            names.add(name);
        }
        if (names.size() > 0) {
            matcher.appendTail(sb);
            String regex2 = sb.toString();

            regex2 = "^"+regex2;
            Pattern urlPattern = Pattern.compile(regex2);
            Matcher urlMatcher = urlPattern.matcher(url);
            if(urlMatcher.matches()) {
                Map<String, String> params = new HashMap<>(names.size());
                for(int i = 1; i <= urlMatcher.groupCount(); i++) {
                    String value = urlMatcher.group(i);
                    //boolean x = matcher2.requireEnd();
                    try {
                        value = URLDecoder.decode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    params.put(names.get(i-1), value);
                }
                return params;
            }
        } else {
            //如果没有变量，返回空map
            if(url.matches(regex)) {
                return new HashMap<>(0);
            }
        }
        //适配失败返回null
        return null;
    }

    
    public static String getAbsoluteUrl(String absolute,String relative) {
        if (StringUtils.isEmpty(relative)) {
            return "";
        }
        if (relative.startsWith("http")) {
            return relative;
        }
        try {
            return new URL(new URL(absolute),relative).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        
    }

    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


}
