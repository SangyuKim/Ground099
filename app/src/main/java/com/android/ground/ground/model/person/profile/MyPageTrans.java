package com.android.ground.ground.model.person.profile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-12.
 */
public class MyPageTrans {
    public int code;
    public String msg;
    @SerializedName("result")
    public ArrayList<MyPageTransResult> items;
}
