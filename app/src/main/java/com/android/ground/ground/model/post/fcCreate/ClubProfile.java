package com.android.ground.ground.model.post.fcCreate;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Tacademy on 2015-11-18.
 */
public class ClubProfile implements Serializable {
    public File mFile;
    public String clubName;
    public String clubLocationName;
    public int clubMainDay_Mon;
    public int clubMainDay_Tue;
    public int clubMainDay_Wed;
    public int clubMainDay_Thu;
    public int clubMainDay_Fri;
    public int clubMainDay_Sat;
    public int clubMainDay_Sun;
    public int memYN;
    public int fieldYN;
    public String clubField;
    public double latitude;
    public double longitude;
    public int matchYN;
    public String clubIntro;

}
