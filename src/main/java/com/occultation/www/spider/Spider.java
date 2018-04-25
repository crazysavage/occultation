package com.occultation.www.spider;

import com.occultation.www.Engine;
import com.occultation.www.SpiderContext;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.render.IPipeline;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liss
 * @created 2017年6月28日 下午1:21:49
 * @version 1.0.0
 */
public class Spider implements Runnable {
    
    private static final Logger log = LoggerFactory.getLogger(Spider.class);

    private int interval;

    private Engine engine;

    private CountDownLatch pauseCdl = new CountDownLatch(0);

    private CountDownLatch completeCdl = new CountDownLatch(0);

    private boolean running;
    
    public Spider(Engine engine, int interva) {
        this.engine = engine;
        this.interval = interva;
    }
    
    @Override
    public void run() {
        SpiderThreadLocal.set(this);
        running = true;
        while(true) {
            if (!running) {
                break;
            }
            try {
                pauseCdl.await();
                completeCdl.await();
            } catch (InterruptedException e) {
                log.error("发生异常.", e);
            }
            
            SpiderRequest req = engine.getQueue().poll();
            if (req == null ) {
                if (engine.notifyComplete()) {
                    break;
                }
                completeCdl = new CountDownLatch(1);
            } else {
                SpiderContext context = engine.getContext(req);
                if (context != null) {
                    SpiderResponse res = context.getFetch().fetch(req);
                    if (res == null) {
                        continue;
                    }
                    int status  = res.getStatus();
                    if (status == 200) {
                        SpiderBean bean = context.getRender().render(context.getBeanClazz(),req,res);
                        IPipeline pipeline = context.getPipeline();
                        if (pipeline != null) {
                            pipeline.process(bean);
                        }
                    } else if (status == 301 || status == 302) {
                        engine.getQueue().offer(req.subRequest(res.getContent()));
                    }
                }

                interval();
            }
        }
    }
    
    public void next() {
        completeCdl.countDown();
    }

    public void stop() {
        this.running = false;
    }
    
    public void pause() {
        pauseCdl = new CountDownLatch(1);
    }
    
    public void restart() {
        pauseCdl.countDown();
    }
    
    private void interval() {
        try {
            Thread.sleep(randomInterval(this.interval));
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
    
    private int randomInterval(int interval) {
        int min = interval - 1000;
        if(min < 1) {
            min = 1;
        }
        int max = interval + 1000;
        return (int)Math.rint(Math.random()*(max-min)+min);
    }

    public Engine getEngine() {
        return engine;
    }

}
