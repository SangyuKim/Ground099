package com.android.ground.ground.manager;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-16.
 */
public class ActivityManager extends AppCompatActivity {
    public ArrayList<Activity> activityArrayList = new ArrayList<Activity>();

    private static ActivityManager instance;
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }


}
