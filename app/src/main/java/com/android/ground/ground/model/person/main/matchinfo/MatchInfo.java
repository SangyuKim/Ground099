package com.android.ground.ground.model.person.main.matchinfo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class MatchInfo implements CheckMatchListData {
    public int code;
    public int matPage;
    public String msg;
    public int itemCount;
    @SerializedName("result")
    public ArrayList<MatchInfoResult> items;

    @Override
    public String toString() {
        return "good " + msg;
    }
}
