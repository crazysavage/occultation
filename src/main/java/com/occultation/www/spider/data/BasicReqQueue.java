package com.occultation.www.spider.data;

import com.occultation.www.net.SpiderRequest;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 13:45
 */
public class BasicReqQueue implements ReqQueue {

    private static final Logger log = LoggerFactory.getLogger(BasicReqQueue.class);

    private ConcurrentLinkedQueue<SpiderRequest> now;

    private ConcurrentLinkedQueue<SpiderRequest> next;

    private Visit visit;

    public BasicReqQueue() {
        now = new ConcurrentLinkedQueue<>();
        next = new ConcurrentLinkedQueue<>();
        visit = new VisitSet();
    }

    public BasicReqQueue(Visit visit) {
        this.visit = visit;
    }

    @Override
    public SpiderRequest poll() {
        SpiderRequest request = now.poll();
        if (request != null) {
            log.debug("consume request [{}]",request.getUrl());
        }
        return request;
    }

    @Override
    public boolean offer(SpiderRequest request) {
        boolean flag = !visit.isVisited(request.getKey()) && next.offer(request);
        if (flag) {
            log.debug("add request [{}]", request.getUrl());
        }
        return flag;
    }

    @Override
    public int size() {
        return now.size();
    }

    @Override
    public void nextQueue() {
        now = next;
        next = new ConcurrentLinkedQueue<>();
    }
}
