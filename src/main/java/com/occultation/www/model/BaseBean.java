package com.occultation.www.model;

import com.alibaba.fastjson.JSON;

/**
 *
 * @author yejy
 * @since 2018-06-13 10:56
 */
public class BaseBean implements SpiderBean{

    @Override
    public String toString() {
        return JSON.toJSONString(this,true);
    }
}
