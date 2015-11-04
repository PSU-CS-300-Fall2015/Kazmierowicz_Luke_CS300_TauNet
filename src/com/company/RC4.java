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
    private int [] stateVector;

    //A variable-length key of from 1 to 256 bytes
    private char [] key;

    private static final int stateVectorLength = 54;
    private static final int minKeyLength = 10;
    private static final int iterationsDiscarded = 0;

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



    public String encrypt(final String msgString) {

        char [] msg = msgString.toCharArray();

        //Stream Generation
        int i, j;
        i = j = 0;

        initStateVector();
        char [] code = new char[msg.length];

        for (int x = 0; x < msg.length; ++x) {
            i = (i+1) % stateVectorLength;
            j = (j + stateVector[i]) % stateVectorLength;
            swap(i, j);
            int k = (stateVector[i] + stateVector[j]) % stateVectorLength;
            code[x] = (char)(k ^ (int)msg[x]);
        }

        return new String(code);
    }


    public String decrypt(final String msg) {
        //Encrypting an encrypted message will yield the original message
        return encrypt(msg);
    }


    private void initStateVector() {

        stateVector  = new int[stateVectorLength];
        int [] tempVector = new int[stateVectorLength];


        for (int i = 0; i < stateVectorLength; ++i) {
            stateVector[i] = i;
            tempVector[i] = key[i % key.length];
        }


        //Discard
        for (int n = 0; n < iterationsDiscarded+1; ++n) {
            //Initial permutation of stateVector
            for (int i = 0, j = 0; i < stateVectorLength; ++i) {
                j = (j + stateVector[i] + tempVector[i]) % stateVectorLength;

                //Swap values in state vector in pos i and j
                swap(i, j);
            }
        }
    }


    //Swap values in state vector in position i and j
    private void swap(int i, int j) {
        int temp = stateVector[i];
        stateVector[i] = stateVector[j];
        stateVector[j] = temp;
    }


}
