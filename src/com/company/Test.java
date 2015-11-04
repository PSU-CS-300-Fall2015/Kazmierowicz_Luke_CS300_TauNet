package com.company;

/**
 * (c) Luke Kazmierowicz 2015
 * CS 300
 * Project: TauNet
 * Filename: Test
 *
 * Purpose:
 *  Contains a suite of test to verify program correctness.
 */
public class Test extends Utility{

    public Test() {

        //Test valid key length
        testKeylen();

    }

    private void testKeylen() {

        boolean didCatch = false;

        String key = "123456789";

        try {
            RC4 rc4 = new RC4(key);
        } catch(InvalidKeyException error) {
            didCatch = true;
        }

        if (!didCatch) {
            println("Failed: Key length min test");
        }

        didCatch = false;

        key = "";
        for (int i = 0; i < 54; ++i) {
            key = key + (i % 10);
        }

        try {
            RC4 rc4 = new RC4(key);
        } catch(InvalidKeyException error) {
            didCatch = true;
        }

        if (!didCatch) {
            println("Failed: Key length max test");
        }
    }

}
