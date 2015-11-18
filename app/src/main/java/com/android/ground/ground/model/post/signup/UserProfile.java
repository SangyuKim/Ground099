package com.android.ground.ground.model.post.signup;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Tacademy on 2015-11-18.
 */
public class UserProfile implements Serializable {
    public File mFile;
    public int gender;
    public int age;
    public String memLocationName;
    public int memMainDay_Mon;
    public int memMainDay_Tue;
    public int memMainDay_Wed;
    public int memMainDay_Thu;
    public int memMainDay_Fri;
    public int memMainDay_Sat;
    public int memMainDay_Sun;
    public String memIntro;
    public int position;
    public int skill;
    public double latitude;
    public double longitude;
}
