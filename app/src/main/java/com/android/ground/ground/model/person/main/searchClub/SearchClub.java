package com.android.ground.ground.model.person.main.searchClub;

import com.android.ground.ground.model.tmap.DongInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-10.
 */
public class SearchClub {
    public int code;
    public String msg;
    public String maxPage;
    public int itemCount;
    @SerializedName("result")
    public ArrayList<SearchClubResult> items;


}
