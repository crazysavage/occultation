package com.occultation.www.net;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-22 11:34
 */
public class FetchFactory {

    private static final String DEF_FETCH = "default";

    private Map<String,IFetch> fetchMap;

    public FetchFactory(Map<String,IFetch> fetchMap) {
        if (MapUtils.isEmpty(fetchMap)) {
            throw new IllegalArgumentException("no fetch can available");
        }
        this.fetchMap = fetchMap;
    }

    public IFetch create() {
        return fetchMap.get(DEF_FETCH);
    }

    public IFetch create(String name) {
        if (!fetchMap.containsKey(name)) {
            name = DEF_FETCH;
        }
        return fetchMap.get(name);
    }

}
