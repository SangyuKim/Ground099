package com.android.ground.ground.model.post.fcUpdate;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Tacademy on 2015-11-18.
 */
public class ClubProfileUpdate implements Serializable {
    public File file;
    public int club_id;
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
