package com.occultation.www.filter;

import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;

/**
 * @Type FilterChain
 * @Desc TODO
 * @author Savage
 * @created 2017年7月4日 下午1:40:28
 * @version 1.0.0
 */
public interface FilterChain {
    void doFilter(SpiderRequest req,SpiderResponse res);
}