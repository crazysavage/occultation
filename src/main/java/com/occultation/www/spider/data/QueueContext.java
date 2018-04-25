package com.occultation.www.spider.data;

import com.occultation.www.net.SpiderRequest;
import com.occultation.www.spider.Spider;
import com.occultation.www.spider.SpiderThreadLocal;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 22:17
 */
public class QueueContext {

    public static void offer(SpiderRequest request) {
        getSpider().getEngine().getQueue().offer(request);
    }


    private static Spider getSpider() {
        Spider spider = SpiderThreadLocal.get();
        if (spider == null) {
            throw new NullPointerException("Thread is not a spider Thread");
        }
        return spider;
    }
}
