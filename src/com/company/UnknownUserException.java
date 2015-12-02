package com.company;

/**
 * Copyright (c) 2015 Luke Kazmierowicz
 * CS 300
 * Project: TauNet
 * Filename: InvalidUsernameException.java
 *
 * Thrown when a username is encountered that is not in the authorized list of users.
 */
public class UnknownUserException extends Exception {
    public UnknownUserException(String message) {
        super(message);
    }
}
