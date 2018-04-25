package com.occultation.www.spider.data;

import com.occultation.www.net.SpiderRequest;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 13:22
 */
public interface ReqQueue {

    /**
     * 从当前爬取队列中获取请求
     * @return
     */
    SpiderRequest poll();

    /**
     * 将请求地址放入到待爬取队列
     * @return
     */
    boolean offer(SpiderRequest request);

    /**
     * 返回当前爬取队列的长度
     * @return
     */
    int size();

    /**
     * 将待爬取队列置于爬取状态
     */
    void nextQueue();

}
