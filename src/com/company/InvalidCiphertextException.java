package com.company;

/**
 * Copyright (c) 2015 Luke Kazmierowicz
 * CS 300
 * Project: TauNet
 * Filename: InvalidKeyException.java
 *
 * Is thrown when ciphertext is too short to have an initialization vector.
 */
public class InvalidCiphertextException extends Exception {
    public InvalidCiphertextException(String message) {
        super(message);
    }
}
