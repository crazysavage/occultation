package com.occultation.www.expections;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-22 17:24
 */
public class NoSuchMethodTypeException extends OccultationException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * constructor
     */
    public NoSuchMethodTypeException() {
        super();
    }

    /**
     *
     * @param msg
     */
    public NoSuchMethodTypeException(String msg) {
        super(msg);
    }

    /**
     *
     * @param e
     */
    public NoSuchMethodTypeException(Throwable e) {
        super(e);
    }

    /**
     *
     * @param msg
     * @param e
     */
    public NoSuchMethodTypeException(String msg, Throwable e) {
        super(msg, e);
    }
}
