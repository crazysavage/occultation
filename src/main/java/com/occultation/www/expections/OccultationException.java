package com.occultation.www.expections;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-08-22 17:24
 */
public class OccultationException extends RuntimeException {
    
    /**
    *
    */
   private static final long serialVersionUID = 1L;

   /**
    * constructor
    */
   public OccultationException() {
       super();
   }

   /**
    *
    * @param msg
    */
   public OccultationException(String msg) {
       super(msg);
   }

   /**
    *
    * @param e
    */
   public OccultationException(Throwable e) {
       super(e);
   }

   /**
    *
    * @param msg
    * @param e
    */
   public OccultationException(String msg, Throwable e) {
       super(msg, e);
   }

}
