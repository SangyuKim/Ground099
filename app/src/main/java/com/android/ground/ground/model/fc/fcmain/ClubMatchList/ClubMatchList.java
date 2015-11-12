package com.android.ground.ground.model.fc.fcmain.ClubMatchList;

import com.android.ground.ground.model.fc.fcmain.ClubAndMember.ClubAndMemberResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-12.
 */
public class ClubMatchList {
    public int code;
    public String msg;
    public int maxPage;
    public int itemCount;
    @SerializedName("result")
    public ArrayList<ClubMatchListResult> items;


}
