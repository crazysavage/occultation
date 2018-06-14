package com.occultation.www.meituan;

import com.occultation.www.EngineBuilder;
import com.occultation.www.net.SpiderRequest;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-12 19:56
 */
public class MeiTuanTask{
    public static void main(String[] args) {
        SpiderRequest request = new SpiderRequest("http://hz.meituan.com/meishi/pn1/");

        EngineBuilder.create().setClasspath("com.occultation.www.meituan")
                .setDeep(1)
                .setInterval(1)
                .setSpiderSize(1)
                .setSeed(request)
                .setCanExtractHref(true)
                .build().start();
    }
}
