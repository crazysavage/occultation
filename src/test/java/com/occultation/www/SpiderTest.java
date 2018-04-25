package com.occultation.www;

import com.occultation.www.net.SpiderRequest;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 16:25
 */
public class SpiderTest {

    public static void main(String[] args) {
        SpiderRequest request = new SpiderRequest("http://www.cnblogs.com/znicy/p/7478371.html");

        EngineBuilder.create().setClasspath("com.occultation.www")
                                .setDeep(3)
                                .setInterval(1)
                                .setSpiderSize(1)
                                .setSeed(request)
                                .setCanExtractHref(true)
                                .build().start();
    }
}
