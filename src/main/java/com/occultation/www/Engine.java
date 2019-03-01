package com.occultation.www;

import com.occultation.www.listener.SpiderListener;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.proxy.ProxyPool;
import com.occultation.www.spider.Spider;
import com.occultation.www.spider.SpiderCreator;
import com.occultation.www.spider.data.ReqQueue;
import com.occultation.www.util.Assert;

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

    private boolean stop;

    private boolean canExtractHref;

    private ProxyPool proxyPool;

    private int completeSize = 0;

    Engine(String classpath) {
        this.contextFactory = new ContextFactory(classpath);
    }

    public void start() {
        log.info("----->>>>start occulttion<<<<-------");
        this.stop = false;

        Assert.isTrue(CollectionUtils.isNotEmpty(seeds),"seed's size is empty");

        this.contextFactory.getListeners().forEach(SpiderListener::beforeStart);

        for (SpiderRequest request : seeds) {
            queue.offer(request);
        }

        cdl = new CountDownLatch(spiderSize);
        spiders = SpiderCreator.create(this,spiderSize,interval);


        int step = 1;
        while (deep < 0 || deep > 0) {
            log.info("------->>>>执行第{}次循环<<<<-------", step++);
            try {
                cdl.await();
            } catch (InterruptedException e) {
                log.error("发生异常.", e);
            }
            if (this.stop) {
                break;
            }
            nextQueue();

        }
    }

    public void stop() {

        this.stop = true;
        for (Spider spider : spiders) {
            spider.stop();
        }
        log.info("spider stop;deep = {}, remain request size = {}", this.deep, this.queue.size());
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
        boolean complete = isComplete();

        if (complete) {
            if (++completeSize == spiderSize) {
                this.contextFactory.getListeners().forEach(SpiderListener::afterComplete);
            }

        } else {
            cdl.countDown();

        }

        return complete;
    }

    public boolean isComplete() {
        return deep == 0;
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

    public ProxyPool getProxyPool() {
        return proxyPool;
    }

    protected void setProxyPool(ProxyPool proxyPool) {
        this.proxyPool = proxyPool;
    }

    public int getDeep() {
        return this.deep;
    }
}
