package com.occultation.www.kuaiyishou;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-07 17:58
 */
public class UrlEncrypt {

    private final static String ex = "video_id=(:.*?)&";
    private final static Pattern p = Pattern.compile(ex);

    private final static int[] KK = {65, 48, 77, 106, 90, 102, 77, 84, 89, 48, 78, 68, 65, 51, 77, 106};

    public static String encrypt(String url) {
        Matcher m = p.matcher(url);
        if (!m.find()) {
            return url;
        }
        String origin = m.group(1);
        String[] parts = origin.split(":");
        int kLen = KK.length;
        StringBuilder res = new StringBuilder("");
        for (int i = 1; i < parts.length; i++) {
            res.append((char)(Integer.valueOf(parts[i]) - (255 & KK[(i-1) % kLen])));
        }
        url = url.replaceAll(ex,"video_id=" + res.toString() + "&");
        if (url.startsWith("http:") || url.startsWith("https:")) {
            return url;
        }
        return "http:" + url;
    }

    public static void main(String[] args) {
        System.out.println(encrypt("//api.amemv.com/aweme/v1/play/?video_id=:183:96:127:154:138:204:133:139:137:96:126:116:163:150:133:156:177:163:126:218:205:209:177:188:187:160:179:116:115:102:180:154&line=0&ratio=720p&media_type=4&vr_type=0&test_cdn=None&improve_bitrate=0"));

    }
}
