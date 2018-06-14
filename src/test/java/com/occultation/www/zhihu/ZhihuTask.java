package com.occultation.www.zhihu;

import com.occultation.www.EngineBuilder;
import com.occultation.www.net.SpiderRequest;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-12 19:56
 */
public class ZhihuTask {
    public static void main(String[] args) {
        SpiderRequest request = new SpiderRequest("https://zhuanlan.zhihu.com/p/21432744");

        EngineBuilder.create().setClasspath("com.occultation.www.zhihu")
                .setDeep(2)
                .setInterval(1)
                .setSpiderSize(1)
                .setSeed(request)
                .setCanExtractHref(true)
                .build().start();
    }
}
