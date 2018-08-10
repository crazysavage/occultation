package com.occultation.www.kuaiyishou;

import com.occultation.www.net.SpiderRequest;
import com.occultation.www.spider.data.ReqQueue;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-08 13:18
 */
public class RevisitReqQueue implements ReqQueue {


    @Override
    public SpiderRequest poll() {
        return new SpiderRequest("https://kuaiyinshi.com/api/dou-yin/recommend/");
    }

    @Override
    public boolean offer(SpiderRequest request) {
        return false;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public void nextQueue() {

    }
}
