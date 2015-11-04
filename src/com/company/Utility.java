package com.company;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * (c) Luke Kazmierowicz 2015
 * CS 300
 * Project: TauNet
 * File Name: Utility.java
 *
 * Utility class to improve input from user and add additional functionality
 * that all classes will use.
 */



/** Utility class declaration
 * ------------------------------------------------------------------------------------*/
public class Utility {

    //For receiving user input
    protected static Scanner input = null;


    //Default Constructor
    public Utility() {

        input = new Scanner(System.in);
    }



    /** Get an integer value from the user and return it.
     * If they enter an invalid character, ask again. */
    protected static int nextInt() {

        //To store the number entered by the user.
        int num;

        //While the user keeps entering invalid characters,
        while (true) {

            //Get the next int.
            try {
                num = input.nextInt();
                input.nextLine();

                //No exception was thrown, return the number.
                return num;

            } //If an exception was thrown,
            catch (InputMismatchException error) {

                //Clear the buffer and ask the user to try again.
                input.nextLine();
                System.out.print("Expected an integer! Try again: ");
            }
        }
    }





    /** Get a double value from the user and return it.
     * If they enter an invalid character, ask again. */
    protected static double nextDouble() {

        //To store the number entered by the user.
        double num;

        //While the user keeps entering invalid characters,
        while (true) {

            //Get the next double from the user.
            try {
                num = input.nextDouble();
                input.nextLine();

                //No exception was thrown, return the number.
                return num;

            } //If an exception was thrown,
            catch (InputMismatchException error) {

                //Clear the buffer and ask the user to try again.
                input.nextLine();
                System.out.print("Expected a decimal number! Try again: ");
            }
        }
    }



    /** Get a float value from the user and return it.
     * If they enter an invalid character, ask again. */
    protected static float nextFloat() {

        //To store the number entered by the user.
        float num;

        //While the user keeps entering invalid characters,
        while (true) {

            //Get the next double from the user.
            try {
                num = input.nextFloat();
                input.nextLine();

                //No exception was thrown, return the number.
                return num;

            } //If an exception was thrown,
            catch (InputMismatchException error) {

                //Clear the buffer and ask the user to try again.
                input.nextLine();
                System.out.print("Expected a decimal number! Try again: ");
            }
        }
    }





    /** Get a line from the user. */
    protected static String nextLine() {
        return input.nextLine();
    }



    /** Print a string to the console. */
    protected static void print(final String str) {
        System.out.print(str);
    }



    /** Print a string to the console with a newline at the end. */
    protected static void println(final String str) {
        System.out.println(str);
    }


    /** Print a newline to the console. */
    protected static void println() {
        System.out.println();
    }




    /** Return the string with the first character made uppercase. */
    protected static String firstUpper(final String str) {

        //Convert the String to an array of characters
        char [] array = str.toCharArray();

        //If the array is not empty,
        if (array.length > 0) {

            //Make the first character uppercase.
            array[0] = Character.toUpperCase(array[0]);

            //Make a String object and return it.
            return new String(array);
        }

        //Return the empty String we were given.
        return str;
    }

}
