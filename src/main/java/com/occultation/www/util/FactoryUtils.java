package com.occultation.www.util;

import com.occultation.www.net.FetchFactory;
import com.occultation.www.render.RenderFactory;
import com.occultation.www.spider.Spider;
import com.occultation.www.spider.SpiderThreadLocal;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-31 12:58
 */
public class FactoryUtils {

    public static RenderFactory getRenderFactory() {
        return getSpider().getEngine().getContextFactory().getRenderFactory();
    }

    public static FetchFactory getFetchFactory() {
        return getSpider().getEngine().getContextFactory().getFetchFactory();
    }

    private static Spider getSpider() {
        Spider spider = SpiderThreadLocal.get();
        if (spider == null) {
            throw new NullPointerException("Thread is not a spider Thread");
        }
        return spider;
    }

}
