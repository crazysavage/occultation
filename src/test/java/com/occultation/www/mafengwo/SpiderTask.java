package com.occultation.www.mafengwo;

import com.occultation.www.EngineBuilder;
import com.occultation.www.net.SpiderRequest;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-26 17:49
 */
public class SpiderTask {

    public static void main(String[] args) {
        SpiderRequest request = new SpiderRequest("http://www.mafengwo.cn/gonglve/ziyouxing/78726.html");


        EngineBuilder.create()
                .setClasspath("com.occultation.www.mafengwo")
                .setDeep(8)
                .setInterval(2000)
                .setSpiderSize(4)
                .setSeed(request)
                .setCanExtractHref(true)
                .build().start();

    }

}
