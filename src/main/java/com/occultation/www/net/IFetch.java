package com.occultation.www.net;

/**
 * @Type IFetch
 * @Desc TODO
 * @author liss
 * @created 2017年6月29日 上午10:43:59
 * @version 1.0.0
 */
public interface IFetch {
    /**
     * 组装http请求，并执行请求，获取文本文档或者json数据
     */
    SpiderResponse fetch(SpiderRequest req);

    void doFetch(SpiderRequest req,SpiderResponse res);
}
