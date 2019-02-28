package com.occultation.www.dazong;

import com.occultation.www.EngineBuilder;
import com.occultation.www.kuaiyishou.RevisitReqQueue;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-06-26 17:49
 */
public class SpiderTask {


    public static void main(String[] args) {
        int max = 50;

        String[] urls = new String[max];
        for (int i = 0; i < max; i++) {
            urls[i] = "http://www.dianping.com/hangzhou/ch10/p" + (i+1);
        }


        EngineBuilder.create()
                .setClasspath("com.occultation.www.dazong")
                .setDeep(1)
                .setInterval(2000)
                .setSpiderSize(1)
                .setSeed(urls)
                .setCanExtractHref(false)
                .build().start();

    }

}
