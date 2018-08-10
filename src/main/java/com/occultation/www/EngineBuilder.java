package com.occultation.www;

import com.google.common.collect.Lists;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.spider.data.ReqQueue;
import com.occultation.www.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 15:53
 */
public class EngineBuilder {

    private int spiderSize = 1;

    private int interval = 1;

    private List<SpiderRequest> seeds;

    private ReqQueue queue;

    private int deep;

    private String classpath;

    private Boolean canExtractHref;

    public static EngineBuilder create() {
        return new EngineBuilder();
    }
    
    private EngineBuilder() {
        super();
    }

    public final EngineBuilder setSpiderSize(int spiderSize) {
        this.spiderSize = spiderSize;
        return this;
    }

    public final EngineBuilder setInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public final EngineBuilder setSeed(SpiderRequest... seeds) {
        this.seeds = Lists.newArrayList(seeds);
        return this;
    }

    public final EngineBuilder setSeed(List<SpiderRequest> seeds) {
        this.seeds = seeds;
        return this;
    }

    public final EngineBuilder setSeed(String... urls) {
        this.seeds = Arrays.stream(urls).map(SpiderRequest::new).collect(Collectors.toList());
        return this;
    }

    public final EngineBuilder setQueue(ReqQueue queue) {
        this.queue = queue;
        return this;
    }

    public final EngineBuilder setDeep(int deep) {
        this.deep = deep;
        return this;
    }

    public final EngineBuilder  setClasspath(String classpath) {
        this.classpath = classpath;
        return this;
    }

    public final EngineBuilder  setCanExtractHref(Boolean canExtractHref) {
        this.canExtractHref = canExtractHref;
        return this;
    }

    public final Engine build() {
        Assert.isTrue(StringUtils.isNotEmpty(classpath),"classpath cant be null");
        Assert.isTrue(CollectionUtils.isNotEmpty(seeds),"seed size is 0");
        Engine engine = new Engine(classpath);
        engine.setSeeds(seeds);
        if (spiderSize < 1) {
            spiderSize = 1;
        }
        engine.setSpiderSize(spiderSize);

        if (interval < 0) {
            interval = 0;
        }
        engine.setInterval(interval);

        if (deep == 0) {
            deep = -1;
        }
        engine.setDeep(deep);

        engine.setQueue(queue);

        if (canExtractHref == null) {
            canExtractHref = false;
        }
        engine.setCanExtractHref(canExtractHref);

        return engine;
    }
}
