package com.company;


/**
 * Copyright (c) 2015 Luke Kazmierowicz
 * CS 300
 * Project: TauNet
 * Filename: CipherSaber2.java
 *
 * References:
 *  Document entitled, THE RC4 STREAM ENCRYPTION ALGORITHM by William Stallings
 *  CipherSaber-2 GitHub repository by Bart Massey
 */

public class CipherSaber2 extends Utility{

    private static final int IV_LENGTH = 10;
    private static final int ROUNDS_OF_KEY_SCHEDULING = 20;
    private static final int MIN_KEY_LENGTH = 8;
    private static final int MAX_KEY_LENGTH = 53;



    /** Ciphersaber-2 encrypt message m with key k and
     * r rounds of key scheduling. */
    public byte [] encrypt(String messageStr, byte [] key) {


        byte [] mes = messageStr.getBytes();

        //Get a key stream from rc4 seeded with current time
        byte [] seed = String.valueOf(System.currentTimeMillis()).getBytes();
        byte [] iv = produceKeyStream(IV_LENGTH, seed);

        //Append the initialization vector to the key
        key = append(key, iv);

        byte [] keyStream = produceKeyStream(mes.length, key);
        byte [] cipherText = new byte[mes.length + IV_LENGTH];

        //Make the first part of the message the unencrypted initialization vector
        System.arraycopy(iv, 0, cipherText, 0, IV_LENGTH);

        //xor the plaintext with the key stream to produce the cipher text
        for (int i = 0; i < mes.length; i++) {
            cipherText[i + IV_LENGTH] = (byte)(mes[i] ^ keyStream[i]);
        }

        return cipherText;
    }



    /** Ciphersaber-2 decrypt ciphertext m with key k and
     * r rounds of key scheduling */
    public String decrypt(byte [] cipherText, byte [] key) throws InvalidCiphertextException {

        //Make sure the cipher text is shorter than the IV
        if (cipherText.length < IV_LENGTH) {
            throw new InvalidCiphertextException("Cipher text cannot be decrypted.");
        }

        //Get the initialization vector
        byte [] iv = new byte[IV_LENGTH];
        System.arraycopy(cipherText, 0, iv, 0, IV_LENGTH);

        //delete the iv from the front of the message
        byte [] mes = new byte[cipherText.length - IV_LENGTH];
        System.arraycopy(cipherText, IV_LENGTH, mes, 0, mes.length);

        //Append the initialization vector to the key
        key = append(key, iv);


        byte [] keyStream = produceKeyStream(mes.length, key);
        char [] plainText = new char[mes.length];

        //xor the cipher text with the key stream to produce the plaintext
        for (int i = 0; i < mes.length; i++) {
            plainText[i] = (char)(mes[i] ^ keyStream[i]);
        }

        return new String(plainText);
    }




    /** Produce an RC4 keystream of length n with
     * r rounds of key scheduling given key k */
    private byte [] produceKeyStream(int keyStreamLength, byte [] key) {

        //Initialize the array.
        int [] S = new int[256];
        for (int i = 0; i < 256; i++) {
            S[i] = i;
        }

        //Do key scheduling.
        int j = 0;
        for (int x = 0; x < ROUNDS_OF_KEY_SCHEDULING; x++) {
            for (int i = 0; i < 256; i++) {
                j = (j + S[i] + (char)key[i % key.length]) % 256;
                int temp = S[i];
                S[i] = S[j];
                S[j] = temp;
            }
        }

        //Finally, produce the stream.
        byte [] keyStream = new byte[keyStreamLength];
        j = 0;
        for (int x = 0; x < keyStreamLength; x++) {
            int i = (x+1) % 256;
            j = (j + S[i]) % 256;

            int temp = S[i];
            S[i] = S[j];
            S[j] = temp;

            keyStream[x] = (byte)S[(S[i] + S[j]) % 256];
        }

        return keyStream;
    }




    /** Append str2 to str1 */
    private byte [] append(byte [] str1, byte [] str2) {

        byte [] str = new byte[str1.length + str2.length];

        System.arraycopy(str1, 0, str, 0, str1.length);
        System.arraycopy(str2, 0, str, str1.length, str2.length);

        return str;
    }


    public static void checkKeyLength(byte [] key) throws InvalidKeyException {

        //Make sure the key is a valid length
        if (key.length < MIN_KEY_LENGTH) {
            throw new InvalidKeyException("Error: The key length is less than " + MIN_KEY_LENGTH +
                    "\nKey length must be between " + MIN_KEY_LENGTH + " and " + MAX_KEY_LENGTH + " (inclusive)");
        } else if (key.length > MAX_KEY_LENGTH) {
            throw new InvalidKeyException("Error: The key length is greater than " + MAX_KEY_LENGTH +
                    "\nKey length must be between " + MIN_KEY_LENGTH + " and " + MAX_KEY_LENGTH + " (inclusive)");
        }
    }





    /** Testing for CipherSaber2 */
    public static void main(String[] args) {

        boolean testsFailed = false;

        //Test decryption against a file known to be encrypted using the CipherSaber2 algorithm
        try {
            String key = "Al";
            CipherSaber2 rc4 = new CipherSaber2();

            //Create array of bytes of known cipher text
            byte [] cipherText = new byte[14];
            cipherText[0] = 0x41;
            cipherText[1] = 0x6C;
            cipherText[2] = 0x20;
            cipherText[3] = 0x44;
            cipherText[4] = 0x61;
            cipherText[5] = 0x6B;
            cipherText[6] = 0x6F;
            cipherText[7] = 0x74;
            cipherText[8] = 0x61;
            cipherText[9] = 0x20;
            cipherText[10] = 0x67;
            cipherText[11] = 0x75;
            cipherText[12] = 0x74;
            cipherText[13] = 0x73;

            //Decrypt the ciphertext
            String message = rc4.decrypt(cipherText, key.getBytes());

            //Check if it worked
            if (!message.equals("held")) {
                testsFailed = true;
            }

        } catch (InvalidCiphertextException e) {
            e.printStackTrace();
        }


        //Test encrypting and decrypting a message
        try {
            String key = "password";
            CipherSaber2 rc4 = new CipherSaber2();


            //Create array of bytes of known cipher text
            String message = "Test message";

            //Decrypt the ciphertext
            byte [] cipherText = rc4.encrypt(message, key.getBytes());

            //Decrypt the ciphertext
            String decryptedMessage = rc4.decrypt(cipherText, key.getBytes());

            //Check if it worked
            if (!decryptedMessage.equals(message)) {
                testsFailed = true;
            }

        } catch (InvalidCiphertextException e) {
            e.printStackTrace();
        }



        //Output if the tests passed of failed
        if (testsFailed) {
            println("\n\n*** CIPHERSABER TEST FAILED ***");
        } else {
            println("\n\n*** All CipherSaber Tests Passed ***");
        }

    }
}
