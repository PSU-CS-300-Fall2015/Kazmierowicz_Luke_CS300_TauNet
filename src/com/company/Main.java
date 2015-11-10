package com.company;

public class Main extends Utility {

    public static void main(String[] args) {

        testRC4();

    }


    private static void testRC4() {

        String message = "mead";

        //Encrypt/decrypt key
        String key = "SomethingCool#";

        CipherSaber2 rc4 = new CipherSaber2();

        try {

            char[] cipherText = rc4.encrypt(message, key.toCharArray());

            println(cipherText);

            String plainText = rc4.decrypt(cipherText, key.toCharArray());

            println(plainText);

        } catch (InvalidKeyException error) {
            println(error.getMessage());
        }
    }
}
