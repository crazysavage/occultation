package com.occultation.www;

import com.occultation.www.model.SpiderBean;
import com.occultation.www.util.BeanUtils;

import java.util.Arrays;
import java.util.List;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-29 13:28
 */
public class ClassTest {

    public static void main(String[] args) {
        Class<TestBean> clazz = TestBean.class;
        SpiderBean bean;
        try {

            bean = clazz.newInstance();
            System.out.println(clazz.getName());
            System.out.println(clazz.getCanonicalName());
            System.out.println(clazz.getSimpleName());
            System.out.println(clazz.getTypeName());
            System.out.println(TestBean.class.isAssignableFrom(clazz));

            BeanUtils.injectField(bean,clazz.getFields()[3], Arrays.asList(new TestBean(),"boy"));
            List<Long> x = ((TestBean)bean).getList();
            System.out.println(x);

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }
}
