package com.company;

public class Main extends Utility{

    public static void main(String[] args) {
	// write your code here

        //Run some tests on every compile
        //Test test = new Test();

        //Encrypt/decrypt key
        String key = "I97o23754o";

        try {

            RC4 rc4 = new RC4(key);

            String secret = "This is a test of CipherSaber.";

            char [] code = rc4.encrypt(secret);

            println("Encrypted code: " + new String(code));

            String message = rc4.decrypt(code);

            println("Decrypted code: " + message);


        } catch(InvalidKeyException error) {
            System.out.println(error.getMessage());
        }

    }

}
