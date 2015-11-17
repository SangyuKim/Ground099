package com.android.ground.ground.model.lineup.result;

import com.android.ground.ground.model.lineup.planLoc.LineupPlanLocResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-17.
 */
public class LineupResult {
    public int code;
    public String msg;
    @SerializedName("result")
    public ArrayList<LineupResultResult> items;

}
