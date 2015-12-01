package com.company;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * (c) Luke Kazmierowicz 2015
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




    private static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
            'B', 'C', 'D', 'E', 'F' };


    public static String toHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_CHARS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
        }
        return new String(hexChars);
    }




    /** Testing for CipherSaber2 */
    public static void main(String[] args) {


        FileInputStream fin = null;
        File file = new File("cstest.cs2");

        try {
            fin = new FileInputStream(file);

            byte fileContent[] = new byte[(int)file.length()];

            fin.read(fileContent);



            println(toHexString(fileContent));


            String key = "asdfg";

            CipherSaber2 rc4 = new CipherSaber2();

            String message = rc4.decrypt(fileContent, key.getBytes());

            println(message);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidCiphertextException e) {
            e.printStackTrace();
        }


    }
}
