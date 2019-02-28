package com.occultation.www.net.proxy;

import com.occultation.www.listener.SpiderListener;
import com.occultation.www.spider.Spider;
import com.occultation.www.util.FileUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: xinyu.ge
 * @Date: 2018/8/15
 */
public class DefaultProxyPool implements ProxyPool {

    private static Logger log = LoggerFactory.getLogger(DefaultProxyPool.class);

    private List<HttpProxy> httpProxies;

    private DelayQueue<DelayedHttpProxy> queue;

    private Integer maxFailNum = 4;

    private Integer permitFailNum = 3;

    private Long delayTime = 300000L;

    public static final String PROXY_POOL_PATH = "classpath:spider-proxy";

    public DefaultProxyPool() {
        init();
    }

    @Override
    public HttpProxy getProxy(HttpProxy httpProxy) {
        if (CollectionUtils.isEmpty(httpProxies)) {
            log.info("没有可用的代理");
            return null;
        }

        HttpProxy now;
        do {
            now = httpProxies.get(0);
            int failNum = now.getFailNum();
            if (failNum > maxFailNum) {
                httpProxies.remove(now);
                now = null;
                log.info("HttpProxy失败次数超过4次，proxy=[{}]", now.toString());

            } else if (failNum > permitFailNum) {
                DelayedHttpProxy delayedHttpProxy = new DelayedHttpProxy(now, delayTime, TimeUnit.MILLISECONDS);
                this.queue.add(delayedHttpProxy);
                httpProxies.remove(now);
                now = null;
            }
        } while (now == null);
        log.info("使用代理{}",now.toString());
        return now;
    }

    public void init() {
        List<String> proxyUrls = FileUtil.readLine(PROXY_POOL_PATH);
        if (CollectionUtils.isEmpty(proxyUrls)) {
            httpProxies = Collections.emptyList();
        } else {
            httpProxies = new ArrayList<>(proxyUrls.size());
            proxyUrls.forEach(url -> {
                try {
                    httpProxies.add(HttpProxy.create(url));
                } catch (Exception e) {
                    log.error("proxy url syntax is error , url = {}", url, e);
                }
            });

            queue = new DelayQueue<>();
            new Thread(() -> {
                for (; ; ) {
                    DelayedHttpProxy delayedHttpProxy = queue.poll();
                    if (delayedHttpProxy != null) {
                        this.httpProxies.add(delayedHttpProxy.getHttpProxy());
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        if (this.maxFailNum == null || this.maxFailNum < 0) {
            this.maxFailNum = 4;
        }

        if (this.delayTime == null || this.delayTime < 0) {
            this.delayTime = 300000L;
        }

        if (this.permitFailNum == null || this.permitFailNum < 0) {
            this.permitFailNum = 3;
        }

    }

    public Integer getMaxFailNum() {
        return maxFailNum;
    }

    public void setMaxFailNum(Integer maxFailNum) {
        this.maxFailNum = maxFailNum;
    }

    public Integer getPermitFailNum() {
        return permitFailNum;
    }

    public void setPermitFailNum(Integer permitFailNum) {
        this.permitFailNum = permitFailNum;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }
}
