package com.company;

public class Main extends Utility{

    public static void main(String[] args) {
	// write your code here

        //Run some tests on every compile
        Test test = new Test();

        //Encrypt/decrypt key
        String key = "I97o23754o";

        try {

            RC4 rc4 = new RC4(key);

            String secret = "Hey man, what's up!";

            String code = rc4.encrypt(secret);

            println("Encrypted code: " + code);

            code = rc4.decrypt(code);

            println("Decrypted code: " + code);


        } catch(InvalidKeyException error) {
            System.out.println(error.getMessage());
        }

        try {

            RC4 rc4 = new RC4(key);

            String secret = "Hey man, what's up!";

            String code = rc4.encrypt(secret);

            println("Encrypted code: " + stringToHex(code));

            code = rc4.decrypt(code);

            println("Decrypted code: " + code);


        } catch(InvalidKeyException error) {
            System.out.println(error.getMessage());
        }

    }

    static String stringToHex(String string) {
        StringBuilder buf = new StringBuilder(200);
        for (char ch: string.toCharArray()) {
            if (buf.length() > 0)
                buf.append(' ');
            buf.append(String.format("%04x", (int) ch));
        }
        return buf.toString();
    }
}
