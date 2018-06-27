package com.occultation.www.render.json;

import static com.occultation.www.util.ClassUtils.getGenericClass;
import static com.occultation.www.util.ClassUtils.isSubType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONPath;
import com.occultation.www.annotation.Json;
import com.occultation.www.enums.RenderTypeEnum;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.render.IFieldRender;
import com.occultation.www.render.type.TypeHandlerRegistry;
import com.occultation.www.util.BeanUtils;
import com.occultation.www.util.FactoryUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-31 14:27
 */
public class JsonFieldRender implements IFieldRender {

    @Override
    @SuppressWarnings({"unchecked"})
    public void fieldRender(SpiderBean bean, SpiderResponse res, SpiderRequest req) {
        Object json = JSON.parseObject(res.getContent());
        Set<Field> fields = ReflectionUtils.getAllFields(bean.getClass(),ReflectionUtils.withAnnotation(Json.class));
        for (Field field : fields) {
            field.setAccessible(true);
            BeanUtils.injectField(bean,field,getValue(json,field,res,req));
        }
    }


    @SuppressWarnings({"unchecked"})
    private Object getValue(Object json, Field field,SpiderResponse res, SpiderRequest req) {
        Class<?> type = field.getType();
        String path = field.getAnnotation(Json.class).value();
        if (StringUtils.isEmpty(path)) {
            path = "$." + field.getName();
        }
        if (!JSONPath.contains(json,path)) {
            return null;
        }
        Object value = JSONPath.eval(json,path);
        if (isSubType(type,List.class)) {
            Class<?> generic = getGenericClass(type);
            if (isSubType(generic, SpiderBean.class)) {
                return listBean((Class<? extends SpiderBean>) type,value,res,req);
            } else {
                return list(type,value);
            }
        }

        if (type.isArray()) {
            Class<?> soloClass = type.getComponentType();
            if (isSubType(soloClass, SpiderBean.class)) {
                return listBean((Class<? extends SpiderBean>) soloClass,value,res,req).toArray();
            } else {
                return list(soloClass,value).toArray();
            }
        }

        if (isSubType(type,SpiderBean.class)) {
            return getBean((Class<? extends SpiderBean>) type,value,res,req);
        }

        return get(type,value);
    }

    private List<SpiderBean> listBean(Class<? extends SpiderBean> type,Object value, SpiderResponse res, SpiderRequest req) {
        List<SpiderBean> list = new ArrayList<>();
        JSONArray jsonArray = (JSONArray)value;
        for (Object o : jsonArray) {
            SpiderBean bean = getBean(type, o, res, req);
            if (bean == null) {
                continue;
            }
            list.add(bean);
        }
        return list;
    }

    private SpiderBean getBean(Class<? extends SpiderBean> type,Object value, SpiderResponse res, SpiderRequest req) {
        if (value == null) {
            return null;
        }
        SpiderResponse next = res.getSubResponse(JSON.toJSONString(value));
        return FactoryUtils.getRenderFactory().create(type).render(type,req,next);
    }

    private List<Object> list(Class<?> type,Object value) {
        List<Object> list = new ArrayList<>();
        JSONArray jsonArray = (JSONArray)value;
        for (Object json : jsonArray) {
            Object obj = get(type,json);
            if (obj == null) {
                continue;
            }
            list.add(obj);
        }
        return list;
    }

    private Object get(Class<?> type,Object value) {
        if (value == null) {
            return null;
        }
        return TypeHandlerRegistry.convert(type,value);

    }


}
