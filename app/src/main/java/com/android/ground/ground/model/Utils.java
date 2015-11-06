package com.android.ground.ground.model;

import java.io.ByteArrayInputStream;

/**
 * Created by Tacademy on 2015-11-06.
 */

public class Utils {
    public static String toString(ByteArrayInputStream is) {
        int size = is.available();
        char[] theChars = new char[size];
        byte[] bytes    = new byte[size];

        is.read(bytes, 0, size);
        for (int i = 0; i < size;)
            theChars[i] = (char)(bytes[i++]&0xff);

        return new String(theChars);
    }
}