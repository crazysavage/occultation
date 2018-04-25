package com.occultation.www.render;

import com.occultation.www.annotation.Href;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.spider.data.QueueContext;
import com.occultation.www.util.BeanUtils;
import com.occultation.www.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-29 10:31
 */
public abstract class AbstractRender implements IRender,IFieldRender  {
    private static final Logger log = LoggerFactory.getLogger(AbstractRender.class);

    @Override
    public SpiderBean render(Class<? extends SpiderBean> beanClazz, SpiderRequest req,SpiderResponse res) {
        try {
            SpiderBean bean = beanClazz.newInstance();
            fieldRender(bean,res,req);
            extractHref(bean,req);
            return bean;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("render {} error",beanClazz.getName(),e);
        }

        return null;
    }

    @SuppressWarnings({"unchecked"})
    public void extractHref(SpiderBean bean,SpiderRequest request) {
        Set<Field> fields = ReflectionUtils.getAllFields(bean.getClass(),ReflectionUtils.withAnnotation(Href.class));
        for (Field field : fields) {
            Object o = BeanUtils.getValue(bean,field);
            if (o == null) {
                continue;
            }
            if (ClassUtils.isSubType(o.getClass(), List.class)) {
                List<String> list = (List<String>) o;
                for (String url : list) {
                    if (StringUtils.isNotEmpty(url)) {
                        QueueContext.offer(request.subRequest(url));
                    }
                }
            } else {
                String url = (String) o;
                if (StringUtils.isNotEmpty(url)) {
                    QueueContext.offer(request.subRequest(url));
                }
            }
        }

    }
}
