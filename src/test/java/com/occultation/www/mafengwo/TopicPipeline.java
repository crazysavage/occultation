package com.occultation.www.mafengwo;

import com.occultation.www.annotation.Pipeline;
import com.occultation.www.mafengwo.model.Mafengwo;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.render.IPipeline;
import com.occultation.www.spider.data.QueueContext;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-26 17:45
 */
@Pipeline("topic")
public class TopicPipeline implements IPipeline {

    private static final String SQL = "INSERT INTO `test`.`mafengwo` (`title`,`remark`,`content`,`views`,`url`) VALUES(#{title},#{remark},#{content},#{views},#{url});";

    @Override
    public void process(SpiderBean bean, SpiderRequest req, SpiderResponse res) {
        TopicBean o = (TopicBean) bean;

        Document doc = Jsoup.parse(o.getRecommend().getHtml(),req.getUrl());
        Elements links = doc.select("a[href]");
        for (Element link : links)  {
            String url = link.absUrl("href");
            if (StringUtils.isNotEmpty(url)) {
                QueueContext.offer(req.subRequest(url));
            }
        }

        Mafengwo mafengwo = new Mafengwo();
        mafengwo.setRemark(o.getRemark());
        mafengwo.setContent(o.getContent());
        mafengwo.setTitle(o.getTitle());
        mafengwo.setUrl(req.getUrl());
        int views = 0;
        try {
            views = Integer.valueOf(o.getReadStr().replaceAll("阅读","").trim());
        } catch (Exception ignored) {}

        mafengwo.setViews(views);
        SqlHelp.getInstance().insert(SQL,mafengwo);

    }
}
