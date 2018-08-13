package com.occultation.www.gif;

import com.occultation.www.EngineBuilder;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-11 23:13
 */
public class SpiderMain {
    public static void main(String[] args) {
        String[] urls = {
                "http://www.yge.cc/dongtaitu/gifchuchu/page/1",
                "http://www.yge.cc/dongtaitu/gifchuchu/page/2",
                "http://www.yge.cc/dongtaitu/gifchuchu/page/3",
                "http://www.yge.cc/dongtaitu/gifchuchu/page/4",
                "http://www.yge.cc/dongtaitu/gifchuchu/page/5",
                "http://www.yge.cc/dongtaitu/gifchuchu/page/6",
                "http://www.yge.cc/dongtaitu/gifchuchu/page/7",
                "http://www.yge.cc/dongtaitu/gifchuchu/page/8",
        };

        EngineBuilder.create()
                .setClasspath("com.occultation.www.yge")
                .setDeep(1)
                .setInterval(1000)
                .setSpiderSize(5)
                .setSeed(urls)
                .setCanExtractHref(true)
                .build().start();

    }

}
