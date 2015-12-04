package com.company;

/**
 * Copyright (c) 2015 Luke Kazmierowicz
 * CS 300
 * Project: TauNet
 * Filename: TauNet.java
 *
 * Stores a message and important information about it.
 * */

public class Message extends Utility {

    private final int MAX_BODY_LENGTH = 934;

    //The version of TauNet the message was created on
    private String versionNumber;

    //The contact of the person who sent the message
    private Contact sender;
    //To store the senders username for if they are not in our contacts
    private String unknownSenderUsername;
    //To mark if the sender was not in our contacts
    private Boolean fromUnknownSender = false;

    //The intended recipient of the message
    private Contact recipient;
    private String unknownRecipientUsername;
    //To mark if we were the intended recipient or not
    private Boolean notIntendedRecipient = false;

    //To store the body of the message
    private String body;
    private Boolean unrecognizedMessage = false;



    /** Default constructor. */
    Message() {
    }


    /** Construct a message to be sent. */
    Message(final String versionNumber, Contact recipient, String sender, String body) throws InvalidMessageException {

        //Check message length
        if (body.length() > MAX_BODY_LENGTH) {
            throw new InvalidMessageException("Message length must be less than " + MAX_BODY_LENGTH + " characters.");
        }

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

        if (recipient == null) {
            try {
                return new Contact("EMPTY-CONTACT", "");
            } catch (InvalidUsernameException e) {
                e.printStackTrace();
            }
        }

        return recipient;
    }


    /** Return a copy of the sender */
    public Contact getSender() {

        if (sender == null) {
            try {
                return new Contact("EMPTY-CONTACT", "");
            } catch (InvalidUsernameException e) {
                e.printStackTrace();
            }
        }

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

        //Check if the version number in the message is a float
        try
        {
            Double.parseDouble(versionNumberTemp);
        }
        catch(NumberFormatException e)
        {
            throw new InvalidMessageException("Can't read message!\n" +
                    "We are unable to recognize this message because it is in the wrong format or sent from a user with a different key.");
        }

        //If the version number doesn't match, bail
        versionNumber = versionNumberTemp;
        if (!TauNet.matchesSystemVersion(versionNumber)) {
            throw new InvalidMessageException("Can't read message!\n" +
                    "We are unable to recognize this message because it was sent from a different version of the TauNet system.");
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

        //Check if we are the intended recipient
        if (notIntendedRecipient = !TauNet.isSystemUsername(recipientTemp)) {
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



    /** Display the username and body */
    public void display() {

        if (unrecognizedMessage) {
            if (body != null) {
                print(body);
            }
            return;
        }

        print("\nMessage from ");
        if (fromUnknownSender) {
            print(unknownSenderUsername);
        }
        else {
            sender.displayUsername();
        }
        print(": ");
        print(body);
    }



    /** Return if the message is from an unknown sender or not. */
    public boolean isFromUnknownSender() {
        return fromUnknownSender;
    }



    /** Testing for Message object. Uncaught exceptions are considered fails.*/
    public static void main(String[] args) {

        //In the following tests any uncaught exception is considered a fail
        boolean testsFailed = false;

        //Test creating a message from a valid formatted string
        try {
            Message mes = new Message("version: 0.2\r\nfrom: LukePi\r\nto: lukekaz9\r\n\r\nHello neighbor,\nHow are you doing?!");
            //mes.display();
        } catch(InvalidMessageException error) {
            println(error.getMessage());
            testsFailed = true;
        }


        //Test creating a message from an invalid formatted string
        try {
            Message mes = new Message(": 0.2\r\nfrom: LukePi\r\nto: lukekaz9\r\n\r\nHello neighbor,\nHow are you doing?!");
            mes.display();
            testsFailed = true;
        } catch(InvalidMessageException error) {
        }


        //Test creating a message with a different version number
        try {
            Message mes = new Message("version: 0.1\r\nfrom: LukePi\r\nto: lukekaz9\r\n\r\nHello neighbor,\nHow are you doing?!");
            mes.display();
            testsFailed = true;
        } catch(InvalidMessageException error) {
        }


        //Test creating an error message
        Message mess = new Message();
        mess.setAsError("This is an error message.");
        if (!mess.body.equals("This is an error message."))
            testsFailed = true;


        //Test getting recipient
        try {
            Message mes = new Message("version: 0.2\r\nfrom: LukePi\r\nto: lukekaz9\r\n\r\nHello neighbor,\nHow are you doing?!");
            Contact test = mes.getRecipient();
        } catch(InvalidMessageException error) {
            testsFailed = true;
        }


        //Test getting sender
        try {
            Message mes = new Message("version: 0.2\r\nfrom: LukePi\r\nto: lukekaz9\r\n\r\nHello neighbor,\nHow are you doing?!");
            Contact test = mes.getRecipient();
        } catch(InvalidMessageException error) {
            testsFailed = true;
        }


        //Output if the tests passed of failed
        if (testsFailed) {
            println("\n\n*** MESSAGE TEST FAILED ***");
        } else {
            println("\n\n*** All Message Tests Passed ***");
        }
    }


}
