package com.occultation.www.net;

import com.occultation.www.filter.DefaultFilterChain;
import com.occultation.www.filter.Filter;
import com.occultation.www.filter.FilterChain;

import java.util.List;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-23 16:49
 */
public abstract class AbstractFetch implements IFetch {

    private List<Filter> filters;


    public SpiderResponse fetch(SpiderRequest req) {
        SpiderResponse response = new SpiderResponse();
        FilterChain chain = new DefaultFilterChain(this,filters);
        chain.doFilter(req,response);
        return response;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
