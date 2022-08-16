package com.varsitycollege.kn_beautyapp_2022_opsc_task2;

import java.util.Random;

public class KeyGenerator {
    //---------------------------------------Code Attribution------------------------------------------------
    //Author:Lion
    //Uses:Create Random String Key
    //variable to contain characters
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    //Method to create a random key
    public static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
    //Link:https://stackoverflow.com/questions/12116092/android-random-string-generator
    //-----------------------------------------------End------------------------------------------------------
}
