package com.occultation.www.gif.yge;

import com.occultation.www.annotation.Pipeline;
import com.occultation.www.mafengwo.SqlHelp;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.render.IPipeline;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-11 23:16
 */
@Pipeline("yge")
public class YgePipeline implements IPipeline {


    private static final Logger log = LoggerFactory.getLogger(YgePipeline.class);

    private static final String SQL = "INSERT INTO `test`.`Gif` (`video_type`,  `gif_url`,  `gif_name`,  `video_no`,  `actress`, `origin_url`) "
            + "VALUES  (#{type},#{gifUrl},#{gifName},#{versionNo},#{actress},#{url})";

    private static final Pattern p1 = Pattern.compile("(（.*?）)?\\s*番号：(\\S*?)\\s*女优：(\\S*)");
    private static final Pattern p2 = Pattern.compile("[番号出处]：(\\S*)");
    private static final Pattern p3 = Pattern.compile("[（(](.*)[)）]");


    @Override
    public void process(SpiderBean bean, SpiderRequest req, SpiderResponse res) {
        ArticleBean article = (ArticleBean) bean;

        extractGifInfo(Jsoup.parse(article.getArticle(),req.getUrl()),req.getUrl());

    }


    private void extractGifInfo(Document doc,String url) {
        try {
            if (!doc.select(".article-meta span > a").get(0).text().contains("GIF")) {
                return;
            }

            Elements es = doc.select("p");
            for (int i=0;i < es.size() ;i++) {

                i = extraImg(es,i,url);

            }
        } catch (Exception e) {
            log.error(" error, url = {}",url,e);
        }

    }

    private int extraImg(Elements es,int i,String ourl) {
        Element node = es.get(i);
        List<String> urls = new ArrayList<>();
        while(node.html().startsWith("<img")) {
            urls.add(node.select("img").get(0).attr("src"));
            node = es.get(++i);
        }
        if (urls.size() == 0) {
            return i;
        }

        GifInfo gif = new GifInfo();
        gif.setUrl(ourl);
        renderGifDesc(gif,node.text());
        gif.setGifName(getName(gif));

        urls.forEach(url -> {
            gif.setGifUrl(url);
            SqlHelp.getInstance().insert(SQL,gif);
            log.info("下载图片，名称 = {}，地址 = {}",gif.getGifName(),gif.getGifUrl());
            PicDownLoad.download(gif.getGifUrl(),gif.getGifName());
        });
        return ++i;
    }

    private void renderGifDesc(GifInfo gif, String desc) {
        if (StringUtils.isNotEmpty(desc)) {

            Matcher m;
            m = p1.matcher(desc);
            if (m.find()) {
                String type = m.group(1);
                if (type != null) {
                    type = type.substring(1, type.length() - 1);
                }
                gif.setType(type);
                gif.setVersionNo(m.group(2));
                gif.setActress(m.group(3).trim());
                return;
            }

            m = p2.matcher(desc);
            if (m.find()) {
                gif.setVersionNo(m.group(1));

                return;
            }

            m = p3.matcher(desc);
            if (m.find()) {
                gif.setVersionNo(m.group(1).replaceAll("[^\\x00-\\xff]", ""));
            }
        }

    }

    private String getName(GifInfo gif) {
        if (gif.getVersionNo() == null) {
            String version = "no." + System.currentTimeMillis();
            gif.setVersionNo(version);
            return version;
        }

        StringBuilder sb = new StringBuilder(gif.getVersionNo());
        if (gif.getActress() != null) {
            sb.append("-");
            sb.append(gif.getActress());
        }
        return sb.toString();

    }


}
