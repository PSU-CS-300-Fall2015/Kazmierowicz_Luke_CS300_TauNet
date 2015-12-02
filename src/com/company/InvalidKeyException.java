package com.company;

/**
 * Copyright (c) 2015 Luke Kazmierowicz
 * CS 300
 * Project: TauNet
 * Filename: InvalidKeyException.java
 *
 * Thrown when a key of invalid length is attempted to be used to encrypt/decrypt
 * a message using a CipherSaber object.
 */
public class InvalidKeyException extends Exception {
    public InvalidKeyException(String message) {
        super(message);
    }
}
