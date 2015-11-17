package com.android.ground.ground.model.lineup.planLoc;

import com.android.ground.ground.model.lineup.match.LineupMatchResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-17.
 */
public class LineupPlanLoc {
    public int code;
    public String msg;
    @SerializedName("result")
    public ArrayList<LineupPlanLocResult> items;

}
