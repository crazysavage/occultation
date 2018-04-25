package com.occultation.www;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-31 14:31
 */
public class FastJsonTest {
    public static void main(String[] args) {
        Integer x = 4;
        System.out.println(JSON.toJSONString(x));
    }
}
