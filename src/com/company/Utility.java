package com.company;


import java.util.Scanner;

/**
 * Copyright (c) 2015 Luke Kazmierowicz
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


    /** Get a line from the user. */
    protected static String nextLine() {
        return input.nextLine();
    }



    /** Print a string to the console. */
    protected static void print(final String str) {
        System.out.print(str);
    }

    /** Print a char array to the console. */
    protected static void print(final char [] str) {
        System.out.print(str);
    }

    /** Print a single character to the console. */
    protected static void print(final char ch) {
        System.out.print(ch);
    }



    /** Print a string to the console with a newline at the end. */
    protected static void println(final String str) {
        System.out.println(str);
    }

    /** Print a char array to the console with a newline at the end. */
    protected static void println(final char [] str) {
        System.out.println(str);
    }


    /** Print a newline to the console. */
    protected static void println() {
        System.out.println();
    }

    /** Delete a line from the console. */
    protected static void deleteln() {

        int count = 1;
        System.out.print(String.format("\033[%dA",count)); // Move up
        System.out.print("\033[2K"); // Erase line content
    }

    /** Clear a line from the console. */
    protected static void clearln() {

        int count = 1;
        System.out.print(String.format("\033[%dA",count)); // Move up
        System.out.print("\033[2K"); // Erase line content
        System.out.println();
    }

}
