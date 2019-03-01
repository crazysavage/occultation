package com.occultation.www.kuaiyishou;

import com.occultation.www.listener.SpiderListener;

/**
 * TODO
 *
 * @author yejy
 * @since 2018-08-13 18:44
 */
public class MyListener implements SpiderListener {

    @Override
    public void beforeStart() {

    }

    @Override
    public void afterComplete() {
        System.out.println("engine is stop");
    }
}
