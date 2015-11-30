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

    private final int MAX_MESSAGE_LENGTH = 500;
    private int portNumber;
    private char [] key;


    public void run() {

        try {
            startListening();
        }
        catch(IOException error) {

            System.out.println("Connection Error!\nMake sure port " + portNumber + " is being forwarded on your router and only one instance of TauNet is running on your network.");
            System.exit(-1);
        }
    }


    Receiver(final char [] key, final int portNumber) {
        this.key = key;
        this.portNumber = portNumber;
    }



    /** Listen for messages and add new ones to the message queue. */
    private void startListening() throws IOException {

        while (true) {

            try (ServerSocket serverSocket = new ServerSocket(portNumber);
                 Socket clientSocket = serverSocket.accept();
                 InputStream in = clientSocket.getInputStream())
            {

                //Get the encrypted bytes from the stream
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[MAX_MESSAGE_LENGTH];
                while ((nRead = in.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                char [] charArray = buffer.toString().toCharArray();

                //Remove null character added by toCharArray
                char [] encryptedMessage = new char[charArray.length-1];
                for (int i = 0; i < encryptedMessage.length; i++) {
                    encryptedMessage[i] = charArray[i];
                }


                //Decrypt message
                try {
                    String messageEncoded = (new CipherSaber2().decrypt(encryptedMessage, key));

                    //println(allInput);
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
                    //Add an unrecognized "flag" message to the queue
                    TauNet.messageQueue.add(new Message());

                } catch (InvalidCiphertextException error) {
                    println(error.getMessage());
                }

            }
        }
    }

}
