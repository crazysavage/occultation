package com.occultation.www.spider.data;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 13:40
 */
public interface Visit {
    /**
     * 判断当前url是否被爬取，或者是否要过滤，如果判断失败，则将该url加入到当前的映射中
     * @param url
     * @return
     */
    boolean isVisited(String url);

}
