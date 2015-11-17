package com.android.ground.ground.model.lineup.scorer;

import com.android.ground.ground.model.lineup.result.LineupResultResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-17.
 */
public class LineupScorer {
    public int code;
    public String msg;
    @SerializedName("result")
    public ArrayList<String> items;
}
