package com.occultation.www.filter;

import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.spider.SpiderThreadLocal;
import com.occultation.www.spider.data.QueueContext;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-05 14:15
 */
public class HerfextractFilter implements Filter{
    @Override
    public void doFilter(SpiderRequest req, SpiderResponse res, FilterChain chain) {
        chain.doFilter(req,res);
        if (SpiderThreadLocal.get().getEngine().getCanExtractHref()
                && res.getContentType()!= null
                && res.getContentType().contains("html")) {
            Document doc = Jsoup.parse(res.getContent(),req.getUrl());
            Elements links = doc.select("a[href]");
            for (Element link : links)  {
                String url = link.absUrl("href");
                if (StringUtils.isNotEmpty(url)) {
                    QueueContext.offer(req.subRequest(url));
                }
            }
        }

    }
}
