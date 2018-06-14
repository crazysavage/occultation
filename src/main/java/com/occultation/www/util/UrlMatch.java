package com.occultation.www.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-12 20:43
 */
public class UrlMatch {

    private static final Pattern PLACEHOLDER = Pattern.compile("\\{(.*?)}");

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
}
