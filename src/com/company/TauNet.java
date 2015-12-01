package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.TreeSet;

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

    private static final int portNumberReceiver = 6283;
    private static final int portNumberSender = 6283;
    private static final String version = "0.2";
    private byte [] universalKey;
    private static String systemUsername;
    private static TreeSet<Contact> users;

    public static Queue<Message> messageQueue;
    public static Message lastMessageReceived;

    private Thread printerThread;

    TauNet() {

        //Load the key, system username and list of contacts from the file
        importData("data.txt");

        //Start message listener on a new thread
        Receiver receiver = new Receiver(universalKey, portNumberReceiver);
        Thread receiverThread = new Thread(receiver);
        receiverThread.setName("Receiver Thread");
        receiverThread.start();

        //Start message printer on a new thread
        DisplayMessages messagePrinter = new DisplayMessages();
        printerThread = new Thread(messagePrinter);
        printerThread.setName("Receiver Thread");
        printerThread.start();


        //Wait for receiver to try to connect
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            println(e.getMessage());
        }


        try {
            presentOptions();
        } catch (InterruptedException e) {
            e.getMessage();
        }


    }


    /** Present the user with options and perform requests.*/
    private void presentOptions() throws InterruptedException {

        println("- Enter 'Q' to quit.");
        println("- Enter 'C' anytime to compose a message.\n- Enter 'R' to reply to the last received message.");
        println("Waiting for incoming messages...");


        while (true) {

            String input;
            input = nextLine();
            clearln();
            if (input.equalsIgnoreCase("c")) {
                composeMessage();

            }//Reply to last received message
            else if (input.equalsIgnoreCase("r")) {
                reply();
            } else if (input.equalsIgnoreCase("q")) {

                println("Program closing...");
                System.exit(0);

            }//Invalid input
            else {

                deleteln();
                print("Must enter 'Q', 'C' or 'R'");

                Thread.sleep(1000);

                println();
                deleteln();
            }
        }
    }



    /** Give the user a list of options. */
    private void composeMessage() {

        //Pause the receiver thread until user input is finished
        printerThread.interrupt();

        println("Authorized TauNet users:");
        displayUsers();

        print("Enter the username you wish to send a message to: ");
        while (true) {
            String input = nextLine();
            try {
                //Get the contact we should send the message to
                Contact recipient = getContactForUsername(input);

                print("Enter your message: ");

                //While the message is invalid, ask again
                while (true) {
                    input = nextLine();

                    Message newMessage = null;
                    try {
                        //Create the message object
                        newMessage = new Message(version, recipient, systemUsername, input);

                        //Send the message
                        Sender sender = new Sender(universalKey, portNumberSender);
                        sender.sendMessage(newMessage);

                        break;
                    }//If the message was invalid inform the user
                    catch (InvalidMessageException error) {
                        println(error.getMessage());

                        print("Enter your message again: ");
                    }
                }

                break;
            }
            catch (IOException error) {
                println(error.getMessage());
                println("Waiting for incoming messages...");
                break;
            }
            catch (UnknownUserException error) {
                print("That user is not in your contact. Try again: ");
            }
        }

        //Inform the receiver thread that it can start receiving again
        printerThread.interrupt();

    }


    /** Let user reply to last received message. */
    private void reply() {

        //If we haven't received any messages yet
        if (lastMessageReceived == null) {
            println("No previous message to reply to! Enter 'C' to compose a new message.");

        }//If the message is from an unknown sender
        else if (lastMessageReceived.isFromUnknownSender()) {
            println("Can't reply to the message because the user is not in your contacts.");
        } else {

            //Pause the receiver thread until user input is finished
            printerThread.interrupt();

            print("Enter your reply to user \"" + lastMessageReceived.getSender().getUsername() + "\": ");

            //Ask for message body until they enter a valid one
            while (true) {

                String input = nextLine();

                //Create a message
                Contact recipient = lastMessageReceived.getSender();
                Message newMessage = null;
                try {
                    newMessage = new Message(version, recipient, systemUsername, input);
                    Sender sender = new Sender(universalKey, portNumberSender);

                    //Try to send the message
                    try {
                        sender.sendMessage(newMessage);
                    } catch (IOException error) {
                        println(error.getMessage());
                    }

                    //They entered a valid message body so we exit loop and stop asking
                    break;
                } catch (InvalidMessageException e) {
                    println(e.getMessage());
                    print("Enter your reply to user \"" + lastMessageReceived.getSender().getUsername() + "\": ");
                }
            }
            //Inform the receiver thread that it can start receiving again
            printerThread.interrupt();

        }
    }



    /** Import user data from file. */
    private void importData(final String fileName) {

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader source = new BufferedReader(fileReader);

            //Call helper method to load the user info from the file
            importData(source);

            source.close();


        }//Catch if the file could not be opened
        catch(FileNotFoundException error) {
            println("The file '" + fileName + "' could not be opened!\nThe TauNet software cannot run without this file."
                    + "\nInstructions on creating this file can be found in the TauNet documentation.");
            System.exit(-1);

        }//The file could not be read
        catch(IOException error) {
            println("There was an error reading the file '" + fileName + "'.\nMake sure it is in the correct format and try again.");
            System.exit(-1);

        }//Invalid encrypt/decrypt key
        catch(InvalidKeyException error) {
            println(error.getMessage() + "\nYou must fix this issue to run the TauNet software.");
            System.exit(-1);

        }//Invalid system username
        catch(InvalidUsernameException error) {
            println("Invalid system username! " + error.getMessage() + "\nYou must choose a valid username to run the TauNet software.");
            System.exit(-1);
        }


    }


    /** Helper method to load the user info from the file. */
    private void importData(BufferedReader source) throws IOException, InvalidKeyException, InvalidUsernameException {

        users = new TreeSet<>();

        //On the first line there will be the encrypt/decrypt key
        universalKey = source.readLine().getBytes();

        CipherSaber2.checkKeyLength(universalKey);

        //The second line will contain the username of this TauNet system
        systemUsername = source.readLine();
        Contact.validateUsername(systemUsername);

        //The third line is blank
        source.readLine();

        //Load the usernames and IP addresses into the list
        String username;
        String IPAddress;
        while((username = source.readLine()) != null && (IPAddress = source.readLine()) != null) {
            users.add(new Contact(username, IPAddress));
        }
    }


    /** Display all contacts. */
    public void displayUsers() {

        for (Contact user : users) {
            print("   ");
            user.display();
            println();
        }
    }


    /** Return if the username is in the contacts. */
    static public boolean isInContacts(final String username) {

        try {
            return users.contains(new Contact(username, ""));
        } catch (InvalidUsernameException error) {
            return false;
        }
    }


    /** Get contact if the username is in the contacts. */
    static public Contact getContactForUsername(final String username) throws UnknownUserException {

        try {
            for (Contact user : users) {
                if (user.equals(new Contact(username, ""))) {
                    return user;
                }
            }

            throw new UnknownUserException("Unknown User");

        } catch (InvalidUsernameException error) {
            throw new UnknownUserException("Invalid and unknown username.");
        }
    }



    /** Return if the username matches the system username. */
    static public boolean isSystemUsername(final String username) {

        return systemUsername.equals(username);

    }


    /** Return if the version number matches. */
    static public boolean matchesSystemVersion(final String ver) {

        return version.equals(ver);

    }


}
