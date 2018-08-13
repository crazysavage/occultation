package com.occultation.www.filter;

import com.google.common.collect.Lists;
import com.occultation.www.net.IFetch;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Type DefaultFilterChain
 * @Desc TODO
 * @author Savage
 * @created 2017年7月4日 下午1:39:22
 * @version 1.0.0
 */
public class DefaultFilterChain implements FilterChain {
    
    private static final Logger log = LoggerFactory.getLogger(DefaultFilterChain.class);

    private Collection<Filter> filters;
    private Iterator<Filter> iterator;
    
    private SpiderRequest req;
    private SpiderResponse res;

    public DefaultFilterChain(IFetch fetch) {
        this(fetch,null);
    }

    public DefaultFilterChain(IFetch fetch,Collection<Filter> filters) {
        if (filters != null) {
            this.filters = Lists.newArrayList(filters);
        } else {
            this.filters = new ArrayList<>();
        }
        this.filters.add(new NetProxyFilter(fetch));
    }

    @Override
    public void doFilter(SpiderRequest req,SpiderResponse res) {

        if (iterator == null) {
            iterator = filters.iterator();
        }
        
        if (this.res != null) {
            log.info("filterChain is used");
            return;
        }
        
        if (iterator.hasNext()) {
            Filter filter = iterator.next();
            filter.doFilter(req,res,this);
        }
        
        this.req = req;
        this.res = res;    
    }
   
    public SpiderRequest getReq() {
        return req;
    }

    public SpiderResponse getRes() {
        return res;
    }


    private static class NetProxyFilter implements Filter {

        private IFetch fetch;
        
        private NetProxyFilter(IFetch fetch) {
            this.fetch = fetch;
        }
        
        @Override
        public void doFilter(SpiderRequest req, SpiderResponse res, FilterChain chain) {
            fetch.doFetch(req, res);
        }
        
    }
    
    
    
    
}
