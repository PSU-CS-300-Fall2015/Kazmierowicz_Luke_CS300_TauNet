package com.company;


/**
 * (c) Luke Kazmierowicz 2015
 * CS 300
 * TauNet
 * Filename: RC4.java
 *
 * Looked at example code entitled "RC4 implementation",
 * posted by Deque on May 16, 2013 on evilzone.org
 */

public class RC4 {

    //state vector
    private int [] stateVector;

    //A variable-length key of from 1 to 256 bytes
    private char [] key;

    private static final int stateVectorLength = 256;
    private static final int minKeyLength = 10;

    public RC4(String key) throws InvalidKeyException {

    }


    private void setKey(String key) throws InvalidKeyException {

        //Make sure the key is a valid length
        if (key.length() < minKeyLength || key.length() >= stateVectorLength) {
            throw new InvalidKeyException("Key length must be between" + minKeyLength + " and " + (stateVectorLength-1));
        }

        this.key = key.toCharArray();
    }

    private void initStateVector() {

        stateVector  = new int[stateVectorLength];
        int [] tempVector = new int[stateVectorLength];


        for (int i = 0; i < stateVectorLength; ++i) {
            stateVector[i] = i;
            tempVector[i] = key[i % key.length];
        }

        //Initial permutation of stateVector
        for (int i = 0, j = 0; i < stateVectorLength; ++i) {
            j = (j + stateVector[i] + tempVector[i]) % stateVectorLength;

            //Swap
            int temp = stateVector[i];
            stateVector[i] = stateVector[j];
            stateVector[j] = temp;
        }
    }


}
