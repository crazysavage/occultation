package com.occultation.www.render;

import com.occultation.www.enums.RenderTypeEnum;
import com.occultation.www.model.HtmlBean;
import com.occultation.www.model.JsonBean;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.render.html.HtmlRender;
import com.occultation.www.render.json.JsonRender;
import com.occultation.www.util.ClassUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-23 17:54
 */
public class RenderFactory {

    Map<Class,IRender> cache = new ConcurrentHashMap<>();

    public IRender create(Class clazz) {
        if (ClassUtils.isSubType(clazz, HtmlBean.class)) {
            return cache.computeIfAbsent(clazz,key -> new HtmlRender());
        } else if (ClassUtils.isSubType(clazz, JsonBean.class)) {
            return cache.computeIfAbsent(clazz,key -> new JsonRender());
        } else {
            throw new IllegalArgumentException("type is undefined");
        }
    }


}
