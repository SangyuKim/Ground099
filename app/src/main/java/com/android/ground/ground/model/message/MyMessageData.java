package com.android.ground.ground.model.message;

import com.android.ground.ground.model.person.profile.MyPageResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-24.
 */
public class MyMessageData {
    public int code;
    public String msg;
    public int maxPage;
    public int itemCount;
    @SerializedName("result")
    public ArrayList<MyMessageDataResult> items;
}
