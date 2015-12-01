package com.company;

/**
 * Copyright (c) Luke Kazmierowicz 2015
 * CS 300
 * Project: TauNet
 * Filename: TauNet.java
 *
 * Stores a message and important information about it.
 *
 * */

public class Message extends Utility {

    private String versionNumber;

    private Contact sender;
    private String unknownSenderUsername;
    private Boolean fromUnknownSender = false;

    private Contact recipient;
    private String unknownRecipientUsername;
    private Boolean notIntendedRecipient = false;

    private String body;

    private Boolean unrecognizedMessage = false;



    /** Construct an message to represent a unrecognizable message. */
    Message() {

    }


    /** Construct a message to be sent. */
    Message(final String versionNumber, Contact recipient, String sender, String body) {

        this.versionNumber = versionNumber;
        this.recipient = recipient;
        try {
            this.sender = new Contact(sender, "");
        } catch(InvalidUsernameException error) {
            print(error.getMessage());
        }
        this.body = body;

    }


    /** Construct a message that was received. */
    Message(final String encodedMessage) throws InvalidMessageException {

        //println(encodedMessage);

        separateMessageParts(encodedMessage);
    }



    public void setAsError(final String errorMessage) {
        unrecognizedMessage = true;
        try {
            sender = new Contact("Unknown", "");
        } catch (InvalidUsernameException e) {
            println(e.getMessage());
        }
        body = errorMessage;
    }



    /** Convert this message into the TauNet protocol format and encrypt it. */
    public String encodedStream() {

        String encoding = "";

        //Create encoded message
        encoding += "version: " + versionNumber + "\r\n";
        encoding += "from: " + sender.getUsername() + "\r\n";
        encoding += "to: " + recipient.getUsername() + "\r\n\r\n";
        encoding += body;

        return encoding;
    }



    /** Return a copy of the recipient */
    public Contact getRecipient() {
        return recipient;
    }


    /** Return a copy of the sender */
    public Contact getSender() {
        return sender;
    }



    /** Extract the information from the string and store it in this message object.
     * @param message
     * @throws InvalidMessageException
     */
    private void separateMessageParts(final String message) throws InvalidMessageException {

        //Get version number
        String versionNumberTemp = "";
        int i = 9;
        for (; (i < message.length() && message.charAt(i) != '\r'); i++) {
            versionNumberTemp += message.charAt(i);
        }
        //Skip newline
        i++;

        //If the version number doesn't match, bail
        versionNumber = versionNumberTemp;
        if (!TauNet.matchesSystemVersion(versionNumber)) {
            throw new InvalidMessageException("Can't read message!\n" +
                    "We are unable to recognize this message because is was sent from a different version of the TauNet system.");
        }


        //Get sender username
        String senderTemp = "";
        for (i += 7; (i < message.length() && message.charAt(i) != '\r'); i++) {
            senderTemp += message.charAt(i);
        }
        //Skip newline
        i++;

        //Get sender contact
        try {
            sender = TauNet.getContactForUsername(senderTemp);
            fromUnknownSender = false;
        }//Flag the message if its from an unknown sender
        catch (UnknownUserException error) {
            fromUnknownSender = true;
            unknownSenderUsername = senderTemp;
        }


        //Get intended recipient username
        String recipientTemp = "";
        for (i += 5; (i < message.length() && message.charAt(i) != '\r'); i++) {
            recipientTemp += message.charAt(i);
        }
        //Skip newline
        i++;

        //Check if we are the intended recipient
        if (notIntendedRecipient =!TauNet.isSystemUsername(recipientTemp)) {
            unknownRecipientUsername = recipientTemp;
        }


        //Skip first three lines to get to message body
        int newlineCount = 0;
        int x = 0;
        for (; (x < message.length() && newlineCount < 4); x++) {
            if (message.charAt(x) == '\n') {
                newlineCount++;
            }
        }
        body = "";
        while (x < message.length()) {

            if (message.charAt(x)  != '\r') {
                body += message.charAt(x);
            }
            x++;
        }
    }


    /** Display the message and all its info. */
    public void displayAll() {

        if (unrecognizedMessage) {
            print(body);
            return;
        }

        println("Version: " + versionNumber);

        print("From: ");
        if (fromUnknownSender) {
            print(unknownSenderUsername);
        }
        else {
            sender.displayUsername();
        }
        println();

        print("To: ");
        if (notIntendedRecipient) {
            println(unknownRecipientUsername);
        } else {
            println("You");
        }

        print(body);
    }



    /** Display the username and body */
    public void display() {

        if (unrecognizedMessage) {
            if (body != null) {
                print(body);
            }
            return;
        }

        if (fromUnknownSender) {
            print(unknownSenderUsername);
        }
        else {
            sender.displayUsername();
        }
        println();
        printBodyIndented();
    }



    /** Display only the message body */
    public void displayBodyOnly() {

        if (unrecognizedMessage) {
            if (body != null) {
                print(body);
            }
            return;
        }

        printBodyIndented();
    }



    /** Print the body to the screen indented one tab. */
    private void printBodyIndented() {

        print("\t");
        for (int i = 0; i < body.length(); i++) {

            print(body.charAt(i));

            //Print a tab after each new line
            if (body.charAt(i) == '\n') {
                print("\t");
            }
        }
    }



    /** Testing for Message object */
    public static void main(String[] args) {

        try {
            Message mes = new Message("version: 1.0\nfrom: LukePi\nto: lukekaz9\n\nHello neighbor,\nHow are you doing?!");
            //print("-");

            mes.display();
        } catch(InvalidMessageException error) {
            println(error.getMessage());
        }

    }


}
