package com.company;


/**
 * (c) Luke Kazmierowicz 2015
 * CS 300
 * Project: TauNet
 * Filename: RC4.java
 *
 * References:
 *  Document entitled, THE RC4 STREAM ENCRYPTION ALGORITHM by William Stallings
 *
 *  Looked at example code entitled "RC4 implementation",
 *  posted by Deque on May 16, 2013 on evilzone.org
 */

public class RC4 {

    //state vector
    private int [] state;

    //A variable-length key of from 1 to 256 bytes
    private char [] key;

    private static final int stateVectorLength = 256;
    private static final int minKeyLength = 10;
    private static final int iterationsDiscarded = 20;

    public RC4(String key) throws InvalidKeyException {
        setKey(key);

    }


    private void setKey(String key) throws InvalidKeyException {

        //Make sure the key is a valid length
        if (key.length() < minKeyLength || key.length() >= stateVectorLength) {
            throw new InvalidKeyException("Error: Key length must be between " + minKeyLength + " and " + (stateVectorLength-1) + " (inclusive)");
        }

        this.key = key.toCharArray();
    }



    public char [] encrypt(final String msg) {
        //Encrypting an encrypted message will yield the original message
        return encrypt(msg.toCharArray());
    }



    public String decrypt(final char [] msg) {
        //Encrypting an encrypted message will yield the original message
        return new String(encrypt(msg));
    }



    private char [] encrypt(final char [] msg) {

        //Stream Generation
        int i, j;
        i = j = 0;

        initStateVector();

        char [] code = new char[msg.length];

        for (int x = 0; x < msg.length; ++x) {
            i = (i+1) % stateVectorLength;
            j = (j + state[i]) % stateVectorLength;
            swap(i, j);
            int k = (state[i] + state[j]) % stateVectorLength;
            code[x] = (char)(k ^ (int)msg[x]);
        }

        return code;
    }



    private void initStateVector() {

        state  = new int[stateVectorLength];
        int [] tempVector = new int[stateVectorLength];


        for (int i = 0; i < stateVectorLength; ++i) {
            state[i] = i;
            tempVector[i] = key[i % key.length];
        }


        //Discard
        for (int n = 0; n < iterationsDiscarded+1; ++n) {
            //Initial permutation of stateVector
            for (int i = 0, j = 0; i < stateVectorLength; ++i) {
                j = (j + state[i] + tempVector[i]) % stateVectorLength;

                //Swap values in state vector in pos i and j
                swap(i, j);
            }
        }
    }


    //Swap values in state vector in position i and j
    private void swap(int i, int j) {
        int temp = state[i];
        state[i] = state[j];
        state[j] = temp;
    }


}
