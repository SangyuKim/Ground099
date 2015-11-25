package com.android.ground.ground.model.message;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-25.
 */
public class ClubMessageData {
    public int code;
    public String msg;
    public int maxPage;
    public int itemCount;
    @SerializedName("result")
    public ArrayList<ClubMessageDataResult> items;

}
