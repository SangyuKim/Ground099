package com.android.ground.ground.model.post.lineup;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-12-02.
 */
public class LineupVirtualFomationPost {
    public int match_id;
    public int club_id;
//    public String locMemInfo;

    @SerializedName("locMemInfo")
    public ArrayList<LineupVirtualFomationPlayerPost> itemslocMemInfo;
    @SerializedName("locVirInfo")
    public ArrayList<LineupVirtualFomationPlayerPost> itemslocVirInfo;
    public int member_id;
}
