package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public Validation() {
    }

    //---------------------------------------Code Attribution------------------------------------------------
    //Author:w3schools
    //Uses:Check if input is Alphabet,Alphanumeric
    public static boolean isAlphabet(String example) {
        boolean isAlpha = example.matches("[a-zA-Z ]+");
        return isAlpha;
        //returns false when string is letters
    }

    //Method to check is string is alphanumeric
    public static boolean isAlphanumeric(String example) {

        Pattern newPatternAlphanumeric = Pattern.compile("^([a-zA-Z0-9]+\\s?)*$");
        Matcher newMatcher = newPatternAlphanumeric.matcher(example);
        boolean isAlphanumeric = newMatcher.matches();
        return isAlphanumeric;
    }
    //Link:https://www.w3schools.com/java/java_regex.asp
    //-----------------------------------------------End------------------------------------------------------

    //Method to check if string is empty
    public static boolean isNullOrEmpty(String example) {

        boolean isNullorEmpty = false;

        if (example == null || example.equals("")) {

            isNullorEmpty = true;
        }
        return isNullorEmpty;
    }

    public static boolean isNumeric(String example) {
        //---------------------------------------Code Attribution------------------------------------------------
        //Author:Nicolas Alarcon Rapela
        //Uses:Check if input is numeric
        Pattern newPatternNumeric = Pattern.compile("\\d+");
        Matcher newMatcher = newPatternNumeric.matcher(example);
        boolean isNumeric = newMatcher.matches();
        return isNumeric;
        //Link:https://stackoverflow.com/questions/15111420/how-to-check-if-a-string-contains-only-digits-in-java
        //-----------------------------------------------End------------------------------------------------------
    }
}

