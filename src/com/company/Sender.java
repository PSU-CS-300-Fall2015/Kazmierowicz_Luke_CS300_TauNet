package com.company;

import java.io.*;
import java.net.*;

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

public class Sender extends Utility {

    private int portNumber;
    private byte [] key;


    /** Sets key and port number. */
    Sender(final byte [] key, final int portNumber) {
        this.key = key;
        this.portNumber = portNumber;
    }

    /** Encrypts and attempts to send the message to it's intended recipient. */
    public void sendMessage(Message message) throws IOException {

        String hostName = message.getRecipient().getIPAddress();

        //We can't send a message to a username if we don't have their IP
        if (hostName == null) {
            println("User not in authorized list!");
        }

        //Open a socket at that IP and port number
        try (Socket socket = new Socket(hostName, portNumber);
             OutputStream out = socket.getOutputStream()) {

            //Encrypt the message
            byte byteArray[] = new CipherSaber2().encrypt(message.encodedStream(), key);

            //Write the message to the socket
            out.write(byteArray);

            socket.close();

            println("Your message was successfully transmitted.");
            println("Waiting for incoming messages...");

        } catch (UnknownHostException e) {
            throw new UnknownHostException("The user \"" + message.getRecipient().getUsername() + "\" is not available. Try again later.");

        } catch (IOException e) {

            throw new IOException("The user \"" + message.getRecipient().getUsername() + " is not available. Try again later.");
        }
    }



}
