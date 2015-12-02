package com.company;

import java.net.*;
import java.io.*;
import java.nio.Buffer;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Copyright (c) Luke Kazmierowicz 2015
 * CS 300
 * Project: TauNet
 * Filename: Receiver.java
 *
 * References:
 *  ORACLE, Java Documentation: Reading from and Writing to a Socket
 *
 * Purpose:
 *  Listen for messages on specified port number and alert the client if new ones come in
 *
 */

public class Receiver extends Utility implements Runnable {

    private final int MAX_MESSAGE_LENGTH = 1024;
    private int portNumber;
    private byte [] key;


    public void run() {

        try {
            startListening();
        }
        catch(IOException error) {

            System.out.println("Connection Error!\nMake sure port " + portNumber + " is being forwarded on your router and only one instance of TauNet is running on your network.");
            System.exit(-1);
        }
    }


    Receiver(final byte [] key, final int portNumber) {
        this.key = key;
        this.portNumber = portNumber;
    }




    /** Listen for messages and add new ones to the message queue. */
    private void startListening() throws IOException {

        //Listen for messages until the end of the program
        while (true) {

            try (ServerSocket serverSocket = new ServerSocket(portNumber);
                 Socket clientSocket = serverSocket.accept();
                 InputStream in = clientSocket.getInputStream())
            {

                //Get all bytes until EOF
                byte [] byteArray = new byte[MAX_MESSAGE_LENGTH];
                int len = 0;

                //Get each byte in the message up to the max message length
                while (len < MAX_MESSAGE_LENGTH) {

                    //Get the integer value of the next byte
                    int nextInt = in.read();
                    //If it's the EOF, exit loop
                    if (nextInt == -1) {
                        break;
                    }
                    //Store it in the byte array
                    byteArray[len] = (byte)nextInt;
                    len++;
                }

                clientSocket.close();
                serverSocket.close();

                //Discard the message if it is empty, otherwise queue it up
                if (len != 0) {

                    //Copy bytes to exact length byte array
                    byte[] encryptedMessage = new byte[len];
                    System.arraycopy(byteArray, 0, encryptedMessage, 0, len);

                    //Decrypt message
                    try {
                        String messageEncoded = (new CipherSaber2().decrypt(encryptedMessage, key));

                        Message mes = new Message(messageEncoded);

                        //Add the new message to the queue
                        if (TauNet.messageQueue == null) {
                            TauNet.messageQueue = new LinkedList<>();
                        }
                        TauNet.messageQueue.add(mes);

                    } catch (InvalidMessageException error) {

                        if (TauNet.messageQueue == null) {
                            TauNet.messageQueue = new LinkedList<>();
                        }
                        Message erMes = new Message();
                        erMes.setAsError(error.getMessage());

                        //Add an unrecognized "flag" message to the queue
                        TauNet.messageQueue.add(erMes);

                    } catch (InvalidCiphertextException error) {
                        println(error.getMessage());
                    }
                }
            }
        }
    }


}
