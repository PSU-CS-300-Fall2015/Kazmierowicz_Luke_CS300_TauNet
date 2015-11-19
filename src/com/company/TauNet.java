package com.company;

/**
 * Copyright (c) Luke Kazmierowicz 2015
 * CS 300
 * Project: TauNet
 * Filename: TauNet.java
 *
 * References:
 *
 *
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
