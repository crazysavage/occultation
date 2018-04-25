package com.occultation.www;

/**
 * @Type IntervalTest
 * @Desc TODO
 * @author Savage
 * @created 2017年7月3日 下午3:33:38
 * @version 1.0.0
 */
public class IntervalTest {
    
    
    public static void main(String[] args) {
        System.out.println(Math.random());
        
        System.out.println(randomInterval(3000)); 
    }
    
    private static int randomInterval(int interval) {
        int min = interval - 1000;
        if(min < 1) {
            min = 1;
        }
        int max = interval + 1000;
        return (int)Math.rint(Math.random()*(max-min)+min);
    }
}
