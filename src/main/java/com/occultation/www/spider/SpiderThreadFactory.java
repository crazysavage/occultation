package com.occultation.www.spider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-08 13:52
 */
public class SpiderThreadFactory implements ThreadFactory {
    private static AtomicInteger count = new AtomicInteger(1);

    private static final Logger log = LoggerFactory.getLogger(SpiderThreadFactory.class);

    @Override
    public Thread newThread(Runnable r) {
        String name = "spider-" + count.getAndIncrement();
        log.info("create spider Thread, name:{}",name);
        return new Thread(r,name);
    }
}
