package com.company;

/**
 * Created by lukekaz9 on 11/19/15.
 */
public class TauNet extends Utility {


    TauNet() {

        //Start message listener on a new thread
        Receiver receiver = new Receiver();
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();
        //receiverThread.join();

        //System.out.println("My turn now!");
    }
}
