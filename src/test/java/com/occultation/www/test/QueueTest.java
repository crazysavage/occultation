package com.occultation.www.test;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 13:27
 */
public class QueueTest  {


    public static void main(String[] args) {

        Queue<String> queue = new ConcurrentLinkedQueue<>();
        queue.offer("hello world");
        queue.offer("java");
        String a = queue.peek();
        a = queue.element();
        a = queue.poll();
        queue.size();
    }
}
