package com.occultation.www;

import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Fetch;
import com.occultation.www.annotation.Pipeline;
import com.occultation.www.enums.RenderTypeEnum;
import com.occultation.www.filter.Filter;
import com.occultation.www.model.HtmlBean;
import com.occultation.www.model.JsonBean;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.AbstractFetch;
import com.occultation.www.net.FetchFactory;
import com.occultation.www.net.IFetch;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.render.IPipeline;
import com.occultation.www.render.IRender;
import com.occultation.www.render.PipelineFactory;
import com.occultation.www.render.RenderFactory;
import com.occultation.www.util.ClassUtils;
import com.occultation.www.util.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 15:25
 */
public class ContextFactory {
    private static final Logger log = LoggerFactory.getLogger(ContextFactory.class);

    private FetchFactory fetchFactory;

    private RenderFactory renderFactory;

    private PipelineFactory pipelineFactory;

    private Map<String,SpiderContext> contextMap;

    public ContextFactory() {
        Reflections reflections = new Reflections(
            ConfigurationBuilder.build("com.occultation.www",
                new SubTypesScanner(),new TypeAnnotationsScanner()));
        init(reflections);
    }

    public ContextFactory(String classPath) {
        //扫描包
        Reflections reflections = new Reflections(
            ConfigurationBuilder.build("com.occultation.www", classPath,
                new SubTypesScanner(),new TypeAnnotationsScanner()));
        init(reflections);

    }

    private void init(Reflections reflections) {
        initFetchFactory(reflections);
        initRenderFactory();
        initPipelineFactory(reflections);
        initSpiderContext(reflections);
    }

    private void initFetchFactory(Reflections reflections) {
        List<Filter> filters = getFilters(reflections);
        Set<Class<?>> fetchs = reflections.getTypesAnnotatedWith(Fetch.class);
        Map<String,IFetch> fetchMap = new HashMap<>();
        for (Class<?> fetchClass : fetchs) {

            if (!ClassUtils.isSubType(fetchClass,IFetch.class)) {
                log.warn("class {} is not IFetch",fetchClass.getTypeName());
                continue;
            }
            Fetch fetchAnnotation = fetchClass.getAnnotation(Fetch.class);
            String key = fetchAnnotation.value();
            if (StringUtils.isEmpty(key)) {
                key = fetchClass.getSimpleName();
            }
            try {
                AbstractFetch fetch = (AbstractFetch) fetchClass.newInstance();
                fetch.setFilters(filters);
                if (fetchMap.containsKey(key)) {
                    log.warn("fetch key {} is exist, the last value will be cover");
                }
                fetchMap.put(key,fetch);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage(),e);
            }

        }
        this.fetchFactory = new FetchFactory(fetchMap);

    }

    private List<Filter> getFilters(Reflections reflections) {
        Set<Class<? extends Filter>> filters = reflections.getSubTypesOf(Filter.class);
        List<Filter> result = new ArrayList<>();
        for (Class<? extends Filter> filter : filters) {
            if (filter.isMemberClass()) {
                continue;
            }
            try {
                result.add(filter.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage(),e);
            }
        }

        return result;
    }

    private void initRenderFactory() {
        this.renderFactory = new RenderFactory();
    }

    private void initPipelineFactory(Reflections reflections) {
        Set<Class<?>> pipelines = reflections.getTypesAnnotatedWith(Pipeline.class);
        Map<String,IPipeline> pipelineMap = new HashMap<>();
        for (Class<?> pipelineClass : pipelines) {

            if (!ClassUtils.isSubType(pipelineClass,IPipeline.class)) {
                log.warn("class {} is not IPipeline",pipelineClass.getTypeName());
                continue;
            }
            Pipeline pipelineAnnotation = pipelineClass.getAnnotation(Pipeline.class);
            String key = pipelineAnnotation.value();
            if (StringUtils.isEmpty(key)) {
                key = pipelineClass.getSimpleName();
            }
            try {
                IPipeline pipeline = (IPipeline) pipelineClass.newInstance();
                if (pipelineMap.containsKey(key)) {
                    log.warn("pipeline key {} is exist, the last value will be cover");
                }
                pipelineMap.put(key,pipeline);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error(e.getMessage(),e);
            }

        }
        this.pipelineFactory = new PipelineFactory(pipelineMap);
    }

    @SuppressWarnings({"unchecked"})
    private void initSpiderContext(Reflections reflections) {
        Set<Class<?>> beans = reflections.getTypesAnnotatedWith(Content.class);
        Map<String,SpiderContext> contextMap = new HashMap<>();
        for (Class<?> bean : beans) {
            Content content = bean.getAnnotation(Content.class);
            IRender render = this.renderFactory.create(bean);
            IFetch fetch = this.fetchFactory.create(content.fetch());
            IPipeline pipeline = this.pipelineFactory.create(content.pipeline());
            SpiderContext context = new SpiderContext((Class<? extends SpiderBean>)bean,fetch,render,pipeline);
            contextMap.put(content.url(),context);
        }
        this.contextMap = contextMap;
    }

    public FetchFactory getFetchFactory() {
        return fetchFactory;
    }

    public RenderFactory getRenderFactory() {
        return renderFactory;
    }

    public PipelineFactory getPipelineFactory() {
        return pipelineFactory;
    }

    public SpiderContext getContext(SpiderRequest req) {
        String url = req.getUrl();
        SpiderContext context = null;
        for (String urlPattern : this.contextMap.keySet()) {

            Map<String, String> params = UrlUtils.match(url,urlPattern);
            if (params != null) {
                req.addParams(params);
                return this.contextMap.get(urlPattern);
            }
            if (urlPattern.equals("*")) {
                context = this.contextMap.get("*");
            }
        }
        return context;
    }
}
