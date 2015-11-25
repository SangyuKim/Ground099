package com.android.ground.ground.model.person.main.matchinfo.matchFormation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-25.
 */
public class MatchFormation {
    public int code;
    public String msg;
    @SerializedName("result")
    public ArrayList<MatchFormationResult> items;
}
