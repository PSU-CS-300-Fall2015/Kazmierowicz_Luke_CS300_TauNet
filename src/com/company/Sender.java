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
    private char [] key;


    Sender(final char [] key, final int portNumber) {
        this.key = key;
        this.portNumber = portNumber;
    }

    public void sendMessage(Message message) throws IOException {

        String hostName = message.getRecipient().getIPAddress();

        if (hostName == null) {
            println("Can't get IP to reply!");
        }

        try (Socket echoSocket = new Socket(hostName, portNumber);
             PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true)) {

            out.println(new CipherSaber2().encrypt(message.encodedStream(), key));

            echoSocket.close();

        } catch (UnknownHostException e) {
            throw new UnknownHostException("The user \"" + message.getRecipient().getUsername() + "\" is not available. Try again later.");

        } catch (IOException e) {

            throw new IOException("The user \"" + message.getRecipient().getUsername() + " is not available. Try again later.");
        }
    }



}
