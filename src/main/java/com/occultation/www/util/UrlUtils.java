package com.occultation.www.util;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;


/**
 * @Type UrlUtil
 * @Desc TODO
 * @author Savage
 * @created 2017年8月21日 下午3:44:22
 * @version 1.0.0
 */
public class UrlUtils {
    
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

}
