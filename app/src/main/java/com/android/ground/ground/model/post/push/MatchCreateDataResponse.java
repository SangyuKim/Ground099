package com.android.ground.ground.model.post.push;

import com.android.ground.ground.model.message.ClubMessageDataResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-30.
 */
public class MatchCreateDataResponse {
    public int code;
    public String msg;
    public MatchCreateDataResponseResult result;
}
