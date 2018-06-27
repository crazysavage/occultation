package com.occultation.www.render.html;

import static com.occultation.www.render.type.TypeHandlerRegistry.convert;

import com.occultation.www.annotation.Html;
import com.occultation.www.enums.RenderTypeEnum;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.util.FactoryUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-29 17:42
 */
public class HtmlParse {
    private static final Logger log = LoggerFactory.getLogger(HtmlParse.class);

    private Document document;

    private SpiderResponse res;

    private SpiderRequest req;

    public HtmlParse(SpiderResponse res,SpiderRequest req) {
        this.res = res;
        this.req = req;
        this.document = Jsoup.parse(res.getContent(),req.getUrl());
    }

    public Object get(Field field) {
        Html html = field.getAnnotation(Html.class);
        Element el = get(html.select());
        return getValue(html, field.getType(), el);
    }


    public List<Object> list(Field field,Class<?> clazz) {
        Html html = field.getAnnotation(Html.class);
        Elements els = document.select(html.select());
        List<Object> list = new ArrayList<>();
        for (Element e : els) {
            list.add(getValue(html,clazz,e));
        }
        return list;
    }

    public List<SpiderBean> listBean(Field field,Class<? extends SpiderBean> clazz) {
        String select = field.getAnnotation(Html.class).select();
        Elements els = document.select(select);
        List<SpiderBean> list = new ArrayList<>();
        for (Element e : els) {
            SpiderBean bean = getBean(e,clazz);
            if (bean == null) {
                continue;
            }
            list.add(bean);
        }
        return list;
    }
    public SpiderBean getBean(Field field,Class<? extends SpiderBean> clazz) {
        String select = field.getAnnotation(Html.class).select();
        Element e = get(select);
        return getBean(e,clazz);
    }

    private SpiderBean getBean(Element e,Class<? extends SpiderBean> clazz) {
        if (e == null) {
            return null;
        }
        String content = e.outerHtml();
        SpiderResponse sub = res.getSubResponse(content);
        return FactoryUtils.getRenderFactory().create(clazz).render(clazz,req,sub);
    }

    private Object getValue(Html html,Class<?> type, Element el) {
        switch (html.type()) {
        case ATTR:
            return convert(type,attr(el,html.attr()));
        case HTML:
            return convert(type,html(el,html.outer()));
        case TEXT:
            return convert(type, text(el));
        default:
            return null;
        }
    }

    private String attr(Element el,String attr) {
        if (el != null) {
            return el.attr(attr);
        }
        return null;
    }

    private String html(Element el,boolean isOuter) {
        if (el != null) {
            if (isOuter) {
                return el.outerHtml();
            }
            return el.html();
        }
        return null;
    }

    private String text(Element el) {
        if (el != null) {
            return el.text();
        }
        return null;
    }

    private Element get(String select) {
        Elements els = document.select(select);
        if (!els.isEmpty()) {
            return els.first();
        }
        log.warn("no element selected,selector: {}",select);
        return null;
    }


}
