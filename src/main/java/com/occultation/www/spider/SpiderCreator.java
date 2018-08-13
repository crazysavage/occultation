package com.occultation.www.spider;

import com.occultation.www.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-13 14:23
 */
public class SpiderCreator {

    private static final Logger log = LoggerFactory.getLogger(SpiderCreator.class);

    private static final int MAX_SPIDER_SIZE = 200;

    public static final SpiderThreadFactory FACTORY =  new SpiderThreadFactory();

    public static List<Spider> create(Engine e,int size,int interval) {
        if (size < 0) {
            size = 1;
        } else if (size > MAX_SPIDER_SIZE) {
            size = MAX_SPIDER_SIZE;
        }

        if (log.isDebugEnabled()) {
            log.debug("spider size = {}",size);
        }

        List<Spider> spiders = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Spider spider = new Spider(e, interval);
            spiders.add(spider);
            FACTORY.newThread(spider).start();
        }
        return spiders;
    }

}
