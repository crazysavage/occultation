package com.occultation.www.render.html;

import static com.occultation.www.util.ClassUtils.isSubType;

import com.occultation.www.annotation.Html;
import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;
import com.occultation.www.render.IFieldRender;
import com.occultation.www.util.BeanUtils;
import com.occultation.www.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.reflections.ReflectionUtils;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-29 16:48
 */
public class HtmlFiledRender implements IFieldRender {

    @Override
    @SuppressWarnings({"unchecked"})
    public void fieldRender(SpiderBean bean, SpiderResponse res, SpiderRequest req) {
        HtmlParse parse = new HtmlParse(res,req);
        Set<Field> fields = ReflectionUtils.getAllFields(bean.getClass(),ReflectionUtils.withAnnotation(Html.class));
        for (Field field : fields) {
            field.setAccessible(true);
            BeanUtils.injectField(bean,field,getValue(field,parse));
        }
    }

    @SuppressWarnings({"unchecked"})
    private Object getValue(Field field,HtmlParse parse) {
        Class<?> type = field.getType();
        if (isSubType(type,List.class)) {
            Class<?> generic = ClassUtils.getGenericClass(field.getGenericType());
            if (isSubType(generic, SpiderBean.class)) {
                return parse.listBean(field, (Class<? extends SpiderBean>) generic);
            } else {
                return parse.list(field,generic);
            }
        }

        if (type.isArray()) {
            Class<?> soloClass = type.getComponentType();
            if (isSubType(soloClass, SpiderBean.class)) {
                List<SpiderBean> beans = parse.listBean(field, (Class<? extends SpiderBean>) soloClass);
                return beans.toArray();
            } else {
                List<Object> objs = parse.list(field,soloClass);
                return objs.toArray();
            }
        }

        if (isSubType(type,SpiderBean.class)) {
            return parse.getBean(field, (Class<? extends SpiderBean>) type);
        }

        return parse.get(field);
    }
}
