package com.android.ground.ground.model.person.profile;

import com.android.ground.ground.model.person.main.matchinfo.MatchInfoResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-12.
 */
public class MyPage {
    public int code;
    public String msg;
    @SerializedName("result")
    public ArrayList<MyPageResult> items;

    @Override
    public String toString() {
        return "good " + msg;
    }

}
