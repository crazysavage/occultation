package com.occultation.www.net.proxy;

import com.occultation.www.util.FileUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-13 16:37
 */
public class SimpleProxyPool implements ProxyPool {

    private static final Logger log = LoggerFactory.getLogger(SimpleProxyPool.class);

    private final List<HttpProxy> httpProxies;

    public SimpleProxyPool() {
        List<String> proxyUrls = FileUtil.readLine("classpath:spider-proxy");
        if (CollectionUtils.isEmpty(proxyUrls)) {
            httpProxies = Collections.emptyList();
        } else {

            httpProxies = new ArrayList<>(proxyUrls.size());
            proxyUrls.forEach(url -> {
                try {
                    httpProxies.add(HttpProxy.create(url));
                } catch (Exception e) {
                  log.error("proxy url syntax is error , url = {}",url,e);
                }
            });
        }

    }

    @Override
    public HttpProxy getProxy(HttpProxy httpProxy) {
        if (httpProxies.size() < 1) {
            return null;
        }
        return httpProxies.get(new Random().nextInt(httpProxies.size()));
    }


}
