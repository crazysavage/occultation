package com.occultation.www;

import java.util.concurrent.CountDownLatch;

/**
 * @Type EngineConfigTest
 * @Desc TODO
 * @author liss
 * @created 2017年7月1日 下午12:42:23
 * @version 1.0.0
 */
public class EngineConfigTest {
    
    private static CountDownLatch cdl;
    
    
    public static void main(String[] args) {
        cdl = new CountDownLatch(5);
        
        for(int i = 0;i<5;i++) {
            new Thread(new Worker(i+"")).start();
        }
        
        int deep = 5;
        while (deep-- > 0) {
            System.out.println("loop start");
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("loop end,deep = "+ deep);
            cdl = new CountDownLatch(5);
        }
    }
    
    static class Worker implements Runnable {

        private String name;
        
        public Worker(String name) {
            this.name = name;
        }
        
        @Override
        public void run() {
            while (cdl.getCount() != 0L) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                System.out.println("worker " + this.name + " is running");
                cdl.countDown();
            }
        }
        
    }
    

}
