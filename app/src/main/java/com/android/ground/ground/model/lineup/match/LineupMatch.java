package com.android.ground.ground.model.lineup.match;

import com.android.ground.ground.model.lineup.info.LineupInfoResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-17.
 */
public class LineupMatch {
    public int code;
    public String msg;
    @SerializedName("result")
    public ArrayList<LineupMatchResult> items;

}
