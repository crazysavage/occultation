package com.occultation.www;

import com.occultation.www.annotation.Content;
import com.occultation.www.annotation.Fetch;
import com.occultation.www.annotation.Inject;
import com.occultation.www.annotation.Pipeline;
import com.occultation.www.expections.BeanDefinedException;
import com.occultation.www.filter.Filter;
import com.occultation.www.listener.SpiderListener;
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 15:25
 */
public class ContextFactory implements BeanContextFactory {
    private static final Logger log = LoggerFactory.getLogger(ContextFactory.class);

    private FetchFactory fetchFactory;

    private RenderFactory renderFactory;

    private PipelineFactory pipelineFactory;

    private List<SpiderListener> listeners;

    private Map<String,SpiderContext> contextMap;

    private static final Map<Class<?>,Object> BEAN_CACHE = new ConcurrentHashMap<>();

    private List<BeanContextFactory> beanContextFactories = new ArrayList<>();

    private static final Object LOCK = new Object();


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
        initListener(reflections);
        initSpiderContext(reflections);
        initBeanContextFactory(reflections);
        initInject(reflections);
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
                AbstractFetch fetch = (AbstractFetch) createBean(fetchClass);
                fetch.setFilters(filters);
                if (fetchMap.containsKey(key)) {
                    log.warn("fetch key {} is exist, the last value will be cover");
                }
                fetchMap.put(key,fetch);
            } catch (Exception e) {
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
                result.add(createBean(filter));
            } catch (Exception e) {
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
                IPipeline pipeline = (IPipeline) createBean(pipelineClass);
                if (pipelineMap.containsKey(key)) {
                    log.warn("pipeline key {} is exist, the last value will be cover");
                }
                pipelineMap.put(key,pipeline);
            } catch (Exception e) {
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

    private void initListener(Reflections reflections) {
        Set<Class<? extends SpiderListener>> listenerClasses =  reflections.getSubTypesOf(SpiderListener.class);
        if (CollectionUtils.isEmpty(listenerClasses)) {
            this.listeners = Collections.emptyList();
        } else {
            List<SpiderListener> instances = new ArrayList<>();
            listenerClasses.forEach(clazz -> {
                try {
                    SpiderListener listener = createBean(clazz);
                    instances.add(listener);
                } catch (Exception e) {
                    log.error("init {} error", clazz.getName(), e);
                }
            });
            this.listeners = instances;
        }
    }

    private void initBeanContextFactory(Reflections reflections) {
        Set<Class<? extends BeanContextFactory>> classes = reflections.getSubTypesOf(BeanContextFactory.class);
        classes.forEach(clazz -> {
            if (clazz.equals(this.getClass())) {
                return;
            }
            beanContextFactories.add(createBean(clazz));
        });

    }

    private void initInject(Reflections reflections) {
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Inject.class);
        for (Class<?> clazz : classes) {
            try {
                Object bean = createBean(clazz);
                injectField(bean,clazz);
            } catch (Exception e) {
                throw new BeanDefinedException("bean inject error",e);
            }
        }
    }

    private void injectField(Object bean,Class<?> clazz) {

        Arrays.stream(clazz.getMethods()).forEach(method -> {
            String methodName = method.getName();
            if (!methodName.startsWith("set") || method.getParameterCount() != 1) {
                return;
            }
            String keyName = methodName.substring(3);
            char[] s = keyName.toCharArray();
            s[0] += 32;
            keyName = String.valueOf(s);
            Class<?> keyType = method.getParameterTypes()[0];

            if (ClassUtils.isBaseType(keyType)) {
                return;
            }

            Object o = getBean(keyName,keyType);
            if (o == null) {
                log.warn("bean name {} type {} is not found",keyName ,keyType);
            }
            method.setAccessible(true);
            try {
                method.invoke(bean, o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("fail to inject via method " + method.getName()
                    + " of interface " + clazz.getName() + ": " + e.getMessage(), e);
            }

        });

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

    public List<SpiderListener> getListeners() {
        return listeners;
    }

    private <T> T createBean(Class<T> clazz) {
        Object bean = BEAN_CACHE.get(clazz);
        if (bean == null) {
            synchronized (LOCK) {
                bean = BEAN_CACHE.get(clazz);
                if (bean == null) {
                    try {
                        bean = clazz.newInstance();
                        BEAN_CACHE.put(clazz,bean);
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException("bean is not found", e);
                    }
                }
            }
        }
        return (T) bean;
    }


    @Override
    public <T> T getBean(String beanName, Class<T> clazz) {

        for (BeanContextFactory beanContextFactory : beanContextFactories) {
            T res = beanContextFactory.getBean(beanName,clazz);
            if (res != null) {
                return res;
            }
        }
        return null;
    }
}
