package com.android.ground.ground.model;

import android.app.Dialog;
import android.view.Window;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.greenfrvr.rubberloader.RubberLoaderView;

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
    public static int[] POSITIONS = new int[]{R.drawable.lw, R.drawable.cf, R.drawable.rw, 4, 5,
            6 ,7, 8, 9, 10, 11, 12, 13
    ,14};

    public static int POSITION_LW = 1;
    public static int POSITION_CF = 2;
    public static int POSITION_RW = 3;
    public static int POSITION_LM = 4;
    public static int POSITION_AM = 5;
    public static int POSITION_RM = 6;
    public static int POSITION_CM = 7;
    public static int POSITION_DM = 8;
    public static int POSITION_LWB = 9;
    public static int POSITION_CB = 10;
    public static int POSITION_RWB = 11;
    public static int POSITION_LB = 12;
    public static int POSITION_RB = 13;
    public static int POSITION_GK = 14;


}