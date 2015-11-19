package com.company;

import java.net.*;
import java.io.*;

/**
 * Copyright (c) Luke Kazmierowicz 2015
 * CS 300
 * Project: TauNet
 * Filename: Receiver.java
 *
 * References:
 *  ORACLE, Java Documentation: Reading from and Writing to a Socket
 *
 */

public class Receiver extends Utility implements Runnable {

    private static final int portNumber = 6284;

    public void run() {

        try {
            startListening();
        }
        catch (IOException error) {
            System.out.println("Connection Error!\nMake sure port " + portNumber + " is being forwarded on your router.");
        }
    }

    private void startListening() throws IOException {

        //Get port number from user
        //print("Enter port number: ");
        //int portNumber = nextInt();


        try (

                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        ) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("New message: ");
                System.out.println(inputLine);
            }
        }
    }



}
