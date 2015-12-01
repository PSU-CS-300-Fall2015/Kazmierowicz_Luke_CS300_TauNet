package com.company;


/**
 * Copyright (c) Luke Kazmierowicz 2015
 * CS 300
 * Project: TauNet
 * Filename: DisplayMessages.java
 *
 * References:
 *  ORACLE, Java Documentation: Reading from and Writing to a Socket
 *
 * Purpose:
 *  Display new messages to screen every so often.
 *
 */

public class DisplayMessages extends Utility implements Runnable {

    private final long refreshTime = 1000;//1 sec

    public void run() {

        //Print any new messages to the screen every so often
        TauNet.lastMessageReceived = null;
        boolean justWaited = true;
        while (true) {

            try {
                //Wait before checking to print new messages again
                Thread.sleep(refreshTime);
            } catch (InterruptedException e) {

                //Wait until other printing to the screen is finished before printing any new messages
                boolean wait = true;
                while (wait) {
                    wait = !Thread.interrupted();
                }

                //Save that we were just waiting for someone else to print to the screen
                justWaited = true;
            }


            //Print all the messages in the queue
            while (TauNet.messageQueue != null && !TauNet.messageQueue.isEmpty()) {
                Message newMessage = TauNet.messageQueue.remove();

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {

                    //Before printing anything to the screen, we need to wait
                    boolean wait = true;
                    while (wait) {
                        wait = !Thread.interrupted();
                    }
                }


                if (newMessage != null) {

                    newMessage.display();
                    println();
                }

                //Save this previous message printed for future reference
                TauNet.lastMessageReceived = newMessage;
                justWaited = false;
            }
        }
    }

}
