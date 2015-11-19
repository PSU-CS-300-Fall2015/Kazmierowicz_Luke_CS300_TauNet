package com.company;

/**
 * Created by lukekaz9 on 11/19/15.
 */

public class HelloRunnable implements Runnable {

    int sharedNumber;

    public void run() {
        System.out.println("Hello from a thread!");

        for (int i = 0; i < 4; i++) {

            sharedNumber = i;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // We've been interrupted: no more messages.
                return;
            }

            System.out.println("Hello from a thread!");
        }

    }


}
