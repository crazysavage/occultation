package com.occultation.www;

import com.occultation.www.net.SpiderRequest;
import com.occultation.www.spider.Spider;
import com.occultation.www.spider.SpiderThreadFactory;
import com.occultation.www.spider.data.BasicReqQueue;
import com.occultation.www.spider.data.ReqQueue;
import com.occultation.www.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Type Engine
 * @Desc TODO
 * @author liss
 * @created 2017年6月29日 下午3:39:20
 * @version 1.0.0
 */
public class Engine {
    private static final Logger log = LoggerFactory.getLogger(Engine.class);

    private int spiderSize;

    private int interval;

    private List<Spider> spiders;

    private List<SpiderRequest> seeds;

    private ReqQueue queue;

    private CountDownLatch cdl;

    private ContextFactory contextFactory;

    private int deep;

    private boolean canExtractHref;

    protected Engine(String classpath) {
        this.contextFactory = new ContextFactory(classpath);
    }

    public void start() {
        log.info("----->>>>start occulttion<<<<-------");
        if (queue == null) {
            queue = new BasicReqQueue();
        }

        Assert.isTrue(CollectionUtils.isNotEmpty(seeds),"seed's size is empty");

        for (SpiderRequest request : seeds) {
            queue.offer(request);
        }

        cdl = new CountDownLatch(spiderSize);
        spiders =  new ArrayList<>(spiderSize);
        SpiderThreadFactory factory = new SpiderThreadFactory();
        for (int i = 0; i < spiderSize; i++) {
            Spider spider = new Spider(this, interval);
            spiders.add(spider);
            factory.newThread(spider).start();
        }
        int step = 0;
        while (deep < 0 || deep > 0) {
            try {
                cdl.await();
            } catch (InterruptedException e) {
                log.error("发生异常.", e);
            }
            nextQueue();
            log.info("------->>>>执行第{}次循环<<<<-------",++step);
        }
    }

    public void stop() {
        for (Spider spider : spiders) {
            spider.stop();
        }
    }

    public void pause() {
        for (Spider spider : spiders) {
            spider.pause();
        }
    }

    public void restart() {
        for (Spider spider : spiders) {
            spider.restart();
        }
    }

    public boolean notifyComplete() {
        cdl.countDown();
        boolean isComplete = (deep == 0);
        if (isComplete){
            log.info("----->>>>end occulttion<<<<-------");
        }
        return isComplete;
    }
    
    private void nextQueue() {
        deep--;
        cdl = new CountDownLatch(spiderSize);
        queue.nextQueue();
        log.info("need fetch queue size is {}",queue.size());
        for (Spider spider : spiders) {
            spider.next();
        }
    }

    public SpiderContext getContext(SpiderRequest req) {
        SpiderContext result = contextFactory.getContext(req);
        if (result == null) {
            log.error("{} is no matcher",req.getUrl());
        }
        return result;
    }

    public boolean getCanExtractHref() {
        return canExtractHref;
    }

    public ReqQueue getQueue() {
        return queue;
    }

    public ContextFactory getContextFactory() {
        return contextFactory;
    }

    protected void setSpiderSize(int spiderSize) {
        this.spiderSize = spiderSize;
    }

    protected void setInterval(int interval) {
        this.interval = interval;
    }

    protected void setSeeds(List<SpiderRequest> seeds) {
        this.seeds = seeds;
    }

    protected void setQueue(ReqQueue queue) {
        this.queue = queue;
    }

    protected void setDeep(int deep) {
        this.deep = deep;
    }

    protected void setCanExtractHref(boolean canExtractHref) {
        this.canExtractHref = canExtractHref;
    }

    public int getDeep() {
        return this.deep;
    }
}
