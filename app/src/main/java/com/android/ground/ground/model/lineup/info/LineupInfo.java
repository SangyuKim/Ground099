package com.android.ground.ground.model.lineup.info;

import com.android.ground.ground.model.fc.fcmain.ClubMatchList.ClubMatchListResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-16.
 */
public class LineupInfo {
    public int code;
    public String msg;
    @SerializedName("result")
    public ArrayList<LineupInfoResult> items;

}
