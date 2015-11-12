package com.android.ground.ground.model.fc.fcmain.clubMain;

import com.android.ground.ground.model.person.main.searchClub.SearchClubResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-12.
 */
public class ClubMain {
    public int code;
    public String msg;
    @SerializedName("result")
    public ArrayList<ClubMainResult> items;

}
