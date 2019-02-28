package com.occultation.www.net.proxy;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: xinyu.ge
 * @Date: 2018/8/15
 */
public class DelayedHttpProxy implements Delayed {

    private HttpProxy httpProxy;

    private Long delayTime;

    private TimeUnit delayTimeUnit;

    private Long executeTime;

    public DelayedHttpProxy(HttpProxy httpProxy, Long delayTime, TimeUnit delayTimeUnit) {
        this.httpProxy = httpProxy;
        this.delayTime = delayTime;
        this.delayTimeUnit = delayTimeUnit;
        this.executeTime = System.currentTimeMillis() + delayTimeUnit.toMillis(delayTime);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(executeTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long result = this.getDelay(TimeUnit.MILLISECONDS)
                - o.getDelay(TimeUnit.MILLISECONDS);
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public HttpProxy getHttpProxy() {
        return httpProxy;
    }

    public Long getDelayYTime() {
        return delayTime;
    }

}
