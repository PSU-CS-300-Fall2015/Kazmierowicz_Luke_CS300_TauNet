package com.company;

/**
 * Copyright (c) 2015 Luke Kazmierowicz
 * CS 300
 * Project: TauNet
 * Filename: TauNet.java
 *
 * Bundle a users information including their IP Address and Username
 *
 * */
public class Contact extends Utility implements Comparable<Contact> {

    private static final int USERNAME_MIN_LENGTH = 3;
    private static final int USERNAME_MAX_LENGTH = 30;

    private String username;
    private String IPAddress;


    /**
     * Builds a contact object from the username and ip address. Makes sure the username is valid.
     * @param username
     * @param IPAddress
     * @throws InvalidUsernameException
     */
    public Contact(final String username, final String IPAddress) throws InvalidUsernameException {

        setUsername(username);
        this.IPAddress = IPAddress;

    }



    /**
     * Print all contact info to display.
     */
    public void display() {
        print("Username: ");
        displayUsername();
    }


    /**
     * Print the username to the screen.
     */
    public void displayUsername() {
        if (username != null) print(username);
    }



    /**
     * Print the IP address to the screen.
     */
    public void displayIPAddress() {
        print(IPAddress);
    }



    /**
     * Set the username after validating it.
     * @param username
     * @throws InvalidUsernameException
     */
    private void setUsername(final String username) throws InvalidUsernameException {

        //Check if username is valid
        validateUsername(username);

        //Our checks didn't throw any exceptions, so the username is valid
        this.username = username;
    }



    /**
     * Supply the contacts ip address
     * @return IPAddress as a string
     */
    public String getIPAddress() {
        return IPAddress;
    }


    /**
     * Supply the contacts username
     * @return Username as a string
     */
    public String getUsername() {
        return username;
    }


    /** Check if a string is a valid username.
     *
     */
    static public void validateUsername(final String username) throws InvalidUsernameException {
        isValidLength(username);
        containsValidCharacters(username);
    }


    /**
     * Checks if the username is a valid length, throws an exception if it's not
     * @param username
     * @throws InvalidUsernameException
     */
    static private void isValidLength(final String username) throws InvalidUsernameException {

        if (username.length() > USERNAME_MAX_LENGTH) {
            throw new InvalidUsernameException("The username is too long. Must be less than " + USERNAME_MAX_LENGTH + " characters.");
        }
        else if(username.length() < USERNAME_MIN_LENGTH) {
            throw new InvalidUsernameException("The username is too short. Must be at least " + USERNAME_MIN_LENGTH + " characters.");
        }
    }



    /**
     * Checks if the username contains valid characters, throws an exception id it doesn't
     * @param username
     * @throws InvalidUsernameException
     */
    static private void containsValidCharacters(final String username) throws InvalidUsernameException {

        for (int i = 0; i < username.length(); i++) {

            if (!(Character.isLetterOrDigit(username.charAt(i)) || (username.charAt(i) == '-'))) {

                throw new InvalidUsernameException("The username contains invalid characters.");
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return !(username != null ? !username.equals(contact.username) : contact.username != null);
    }


    @Override
    public int compareTo(Contact o) {

        //compare name
        return username.compareToIgnoreCase(o.username);

    }

    @Override
    public String toString() {
        return username + ": " + IPAddress;
    }





    /** Testing for Contact object */
    public static void main(String[] args) {

        // For each of the following tests the expected output is
        // username of the message. A failed test is indicated by
        // an error message
        //-----------------------------------------------------------
        boolean testsFailed = false;
        Contact luke;
        try {
            //Letters
            luke = new Contact("lukekaz", "198.29.41.107");
            luke.display();
            println();

            println(luke.getIPAddress());

        } catch(InvalidUsernameException error) {
            println(error.getMessage());
            testsFailed = true;
        }


        try {
            //Contains -
            luke = new Contact("lukekaz-", "198.29.41.107");
            luke.display();
            println();


        } catch(InvalidUsernameException error) {
            println(error.getMessage());
            testsFailed = true;
        }


        try {
            //Numbers
            luke = new Contact("lukekaz9", "198.29.41.107");
            luke.display();
            println();

        } catch(InvalidUsernameException error) {
            println(error.getMessage());
            testsFailed = true;
        }


        // For each of the following tests the expected output is
        // an error message. If no error message is displayed the
        // test has failed.
        //-----------------------------------------------------------
        try {
            //Too short
            luke = new Contact("lu", "198.29.41.107");
            luke.display();
            println();

            testsFailed = true;

        } catch(InvalidUsernameException error) {
            println(error.getMessage());
        }


        try {
            //Too Long
            luke = new Contact("LukeKazmierowicz-LukeKazmierowicz", "198.29.41.107");
            luke.display();
            println();

            testsFailed = true;

        } catch(InvalidUsernameException error) {
            println(error.getMessage());
        }


        try {
            //Invalid characters
            luke = new Contact("lukekaz_", "198.29.41.107");
            luke.display();
            println();

            testsFailed = true;

        } catch(InvalidUsernameException error) {
            println(error.getMessage());
        }


        //Test equals method
        try {
            println();
            println();

            //Invalid characters
            luke = new Contact("lukekaz9", "198.29.41.107");
            luke.display();
            println();

            Contact contact = new Contact("lukekaz9", "41");
            contact.display();
            println();

            if (luke.equals(contact)) {
                println("They match!");
            } else {
                println("They don't match.");
                testsFailed = true;
            }

        } catch(InvalidUsernameException error) {
            println(error.getMessage());
            testsFailed = true;
        }



        //Output if the tests passed of failed
        if (testsFailed) {
            println("\n\n*** CONTACT TEST FAILED ***");
        } else {
            println("\n\n*** All Contact Tests Passed ***");
        }

    }

}
