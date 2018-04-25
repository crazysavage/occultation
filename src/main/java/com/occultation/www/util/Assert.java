package com.occultation.www.util;

/**
 * @Type Assert
 * @Desc TODO
 * @author Savage
 * @created 2017年8月18日 下午2:45:18
 * @version 1.0.0
 */
public class Assert {
    
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

}
