package com.occultation.www.filter;

import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;

/**
 * @Type Filter
 * @Desc TODO
 * @author Savage
 * @created 2017年7月4日 下午1:40:46
 * @version 1.0.0
 */
public interface Filter {
    void doFilter(SpiderRequest req,SpiderResponse res,FilterChain chain);
}
