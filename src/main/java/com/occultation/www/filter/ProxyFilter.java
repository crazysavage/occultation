package com.occultation.www.filter;

import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.net.proxy.HttpProxy;
import com.occultation.www.spider.SpiderThreadLocal;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-13 16:26
 */
public class ProxyFilter implements Filter {


    @Override
    public void doFilter(SpiderRequest req, SpiderResponse res, FilterChain chain) {
        HttpProxy httpProxy =  SpiderThreadLocal.get().getEngine().getProxyPool().getProxy(req.getProxy());
        req.setProxy(httpProxy);

        try {
            chain.doFilter(req,res);
        } catch (Exception e) {
            if (httpProxy != null) {
                httpProxy.fail();
            }
            throw e;
        }

        if (httpProxy == null) {
            return;
        }

        if (res.getStatus() != 200) {
            httpProxy.fail();
        } else {
            httpProxy.success();
        }

    }
}
