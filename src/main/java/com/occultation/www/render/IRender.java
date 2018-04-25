package com.occultation.www.render;

import com.occultation.www.model.SpiderBean;
import com.occultation.www.net.SpiderRequest;
import com.occultation.www.net.SpiderResponse;

/**
 * @Type IRender
 * @Desc TODO
 * @author liss
 * @created 2017年7月1日 下午6:03:24
 * @version 1.0.0
 */
public interface IRender {

    SpiderBean render(Class<? extends SpiderBean> beanClazz, SpiderRequest req,SpiderResponse res);
}
