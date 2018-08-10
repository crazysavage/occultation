package com.occultation.www.kuaiyishou;

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
        String[] urls = {
                "https://kuaiyinshi.com/api/dou-yin/recommend/",
                "https://kuaiyinshi.com/api/dou-yin/recommend/",
                "https://kuaiyinshi.com/api/dou-yin/recommend/"
        };

        EngineBuilder.create()
                .setClasspath("com.occultation.www.kuaiyishou")
                .setDeep(1)
                .setInterval(1000)
                .setSpiderSize(3)
                .setSeed(urls)
                .setQueue(new RevisitReqQueue())
                .setCanExtractHref(false)
                .build().start();

    }

}
