package com.occultation.www.render;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-24 10:01
 */
public class PipelineFactory {

    private Map<String,IPipeline> pipelineMap;

    public PipelineFactory(Map<String,IPipeline> fetchMap) {
        if (MapUtils.isEmpty(fetchMap)) {
            fetchMap = new HashMap<>();
        }
        this.pipelineMap = fetchMap;
    }


    public IPipeline create(String name) {
        return pipelineMap.get(name);
    }
}
