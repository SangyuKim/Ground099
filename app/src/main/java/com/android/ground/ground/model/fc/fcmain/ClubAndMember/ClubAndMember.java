package com.android.ground.ground.model.fc.fcmain.ClubAndMember;

import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMainResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-12.
 */
public class ClubAndMember {
    public int code;
    public String msg;
    @SerializedName("result")
    public ArrayList<ClubAndMemberResult> items;

}
