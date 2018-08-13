package com.occultation.www.spider;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-31 12:51
 */
public class SpiderThreadLocal {
    private final static ThreadLocal<Spider> VAR = new ThreadLocal<>();

    public static void set(Spider spider) {
        VAR.set(spider);
    }

    public static Spider get() {
        return VAR.get();
    }

    public static void clear() {
        VAR.remove();
    }
}
