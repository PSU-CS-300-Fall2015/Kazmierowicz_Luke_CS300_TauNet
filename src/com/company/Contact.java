package com.company;

/**
 * Copyright (c) Luke Kazmierowicz 2015
 * CS 300
 * Project: TauNet
 * Filename: TauNet.java
 *
 * Bundle a users information including their IP Address and Username
 *
 * */
public class Contact extends Utility {

    private static final int USERNAME_MIN_LENGTH = 3;
    private static final int USERNAME_MAX_LENGTH = 12;

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
     * Print the username to the screen.
     */
    public void display() {
        print(username);
    }



    /**
     * Set the username after validating it.
     * @param username
     * @throws InvalidUsernameException
     */
    private void setUsername(String username) throws InvalidUsernameException {

        //Check if username is valid
        isValidLength(username);
        containsValidCharacters(username);

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
     * Checks if the username is a valid length, throws an exception if it's not
     * @param username
     * @throws InvalidUsernameException
     */
    private void isValidLength(final String username) throws InvalidUsernameException {

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
    private void containsValidCharacters(final String username) throws InvalidUsernameException {

        for (int i = 0; i < username.length(); i++) {

            if (!(Character.isLetterOrDigit(username.charAt(i)) || (username.charAt(i) == '-'))) {

                throw new InvalidUsernameException("The username contains invalid characters.");
            }
        }
    }





    /** Testing for Contact object */
    public static void main(String[] args) {

        Contact luke;

        try {
            //Letters
            luke = new Contact("lukekaz", "198.29.41.107");
            luke.display();
            println();

            println(luke.getIPAddress());

        } catch(InvalidUsernameException error) {
            println(error.getMessage());
        }


        try {
            //Contains -
            luke = new Contact("lukekaz-", "198.29.41.107");
            luke.display();
            println();


        } catch(InvalidUsernameException error) {
            println(error.getMessage());
        }


        try {
            //Numbers
            luke = new Contact("lukekaz9", "198.29.41.107");
            luke.display();
            println();

        } catch(InvalidUsernameException error) {
            println(error.getMessage());
        }


        try {
            //Too short
            luke = new Contact("lu", "198.29.41.107");
            luke.display();
            println();

        } catch(InvalidUsernameException error) {
            println(error.getMessage());
        }


        try {
            //Too Long
            luke = new Contact("lukekazmierowicz", "198.29.41.107");
            luke.display();
            println();

        } catch(InvalidUsernameException error) {
            println(error.getMessage());
        }


        try {
            //Invalid characters
            luke = new Contact("lukekaz_", "198.29.41.107");
            luke.display();
            println();

        } catch(InvalidUsernameException error) {
            println(error.getMessage());
        }

    }

}
