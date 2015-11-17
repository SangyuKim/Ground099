package com.android.ground.ground.model.lineup.virtual.fomation;

import com.android.ground.ground.model.lineup.match.LineupMatchResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-17.
 */
public class LineupVirtualFomation {
    public int code;
    public String msg;
    @SerializedName("result")
    public ArrayList<LineupVirtualFomationResult> items;

}
