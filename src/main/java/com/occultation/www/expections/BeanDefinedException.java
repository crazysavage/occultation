package com.occultation.www.expections;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-29 13:13
 */
public class BeanDefinedException extends OccultationException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * constructor
     */
    public BeanDefinedException() {
        super();
    }

    /**
     *
     * @param msg
     */
    public BeanDefinedException(String msg) {
        super(msg);
    }

    /**
     *
     * @param e
     */
    public BeanDefinedException(Throwable e) {
        super(e);
    }

    /**
     *
     * @param msg
     * @param e
     */
    public BeanDefinedException(String msg, Throwable e) {
        super(msg, e);
    }
}
