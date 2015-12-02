package com.company;

/**
 * Copyright (c) 2015 Luke Kazmierowicz
 * CS 300
 * Project: TauNet
 * Filename: InvalidMessageException.java
 *
 * Thrown when a uninterpretable message is encountered.
 */
public class InvalidMessageException extends Exception {
    public InvalidMessageException(String message) {
        super(message);
    }
}
