package com.company;

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
    public char [] encrypt(String messageStr, char [] key) {

        char [] mes = messageStr.toCharArray();

        //Get a key stream from rc4 seeded with current time
        char [] seed = String.valueOf(System.currentTimeMillis()).toCharArray();
        char [] iv = produceKeyStream(IV_LENGTH, seed);

        iv = "1234567890".toCharArray();

        //Append the initialization vector to the key
        key = append(key, iv);

        char [] keyStream = produceKeyStream(mes.length, key);
        char [] cipherText = new char[mes.length + IV_LENGTH];

        //Make the first part of the message the unencrypted initialization vector
        System.arraycopy(iv, 0, cipherText, 0, IV_LENGTH);

        //xor the plaintext with the key stream to produce the cipher text
        for (int i = 0; i < mes.length; i++) {
            cipherText[i + IV_LENGTH] = (char)(mes[i] ^ keyStream[i]);
        }

        return cipherText;
    }



    /** Ciphersaber-2 decrypt ciphertext m with key k and
     * r rounds of key scheduling */
    public String decrypt(char [] cipherText, char [] key) throws InvalidCiphertextException {

        //Make sure the cipher text is shorter than the IV
        if (cipherText.length < IV_LENGTH) {
            throw new InvalidCiphertextException("Cipher text cannot be decrypted.");
        }

        //Get the initialization vector
        char [] iv = new char[IV_LENGTH];
        System.arraycopy(cipherText, 0, iv, 0, IV_LENGTH);

        //delete the iv from the front of the message
        char [] mes = new char[cipherText.length - IV_LENGTH];
        System.arraycopy(cipherText, IV_LENGTH, mes, 0, mes.length);

        //Append the initialization vector to the key
        key = append(key, iv);

        char [] keyStream = produceKeyStream(mes.length, key);
        char [] plainText = new char[mes.length];

        //xor the cipher text with the key stream to produce the plaintext
        for (int i = 0; i < mes.length; i++) {
            plainText[i] = (char)(mes[i] ^ keyStream[i]);
        }

        return new String(plainText);
    }




    /** Produce an RC4 keystream of length n with
     * r rounds of key scheduling given key k */
    private char [] produceKeyStream(int keyStreamLength, char [] key) {

        //Initialize the array.
        int [] S = new int[256];
        for (int i = 0; i < 256; i++) {
            S[i] = i;
        }

        //Do key scheduling.
        int j = 0;
        for (int x = 0; x < ROUNDS_OF_KEY_SCHEDULING; x++) {
            for (int i = 0; i < 256; i++) {
                j = (j + S[i] + key[i % key.length]) % 256;
                int temp = S[i];
                S[i] = S[j];
                S[j] = temp;
            }
        }

        //Finally, produce the stream.
        char [] keyStream = new char[keyStreamLength];
        j = 0;
        for (int x = 0; x < keyStreamLength; x++) {
            int i = (x+1) % 256;
            j = (j + S[i]) % 256;

            int temp = S[i];
            S[i] = S[j];
            S[j] = temp;

            keyStream[x] = (char)S[(S[i] + S[j]) % 256];
        }

        return keyStream;
    }




    /** Append str2 to str1 */
    private char [] append(char [] str1, char [] str2) {

        char [] str = new char[str1.length + str2.length];

        System.arraycopy(str1, 0, str, 0, str1.length);
        System.arraycopy(str2, 0, str, str1.length, str2.length);

        return str;
    }


    public static void checkKeyLength(char [] key) throws InvalidKeyException {

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


        String message = "Hey hows it going? My name is kaz. What's up!";

        //Encrypt/decrypt key
        String key = "Applesauce42";

        CipherSaber2 rc4 = new CipherSaber2();


        for (int i = 0; i < 1; i++) {
            try {

                CipherSaber2.checkKeyLength(key.toCharArray());

                char[] cipherText = rc4.encrypt(message, key.toCharArray());

                println(cipherText);

                try {
                    String plainText = rc4.decrypt(cipherText, key.toCharArray());
                    //println(plainText);
                    if (!plainText.equals(message)) {
                        println("Failed test!");
                        break;
                    }
                } catch (InvalidCiphertextException error) {
                    println(error.getMessage());
                }

            } catch (InvalidKeyException error) {
                println(error.getMessage());
            }
        }
    }
}
