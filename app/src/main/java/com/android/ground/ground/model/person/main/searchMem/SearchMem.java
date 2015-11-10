package com.android.ground.ground.model.person.main.searchMem;

import com.android.ground.ground.model.person.main.searchClub.SearchClubResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-10.
 */
public class SearchMem {
    public int code;
    public String msg;
    public int maxPage;
    public int itemCount;
    @SerializedName("result")
    public ArrayList<SearchMemResult> items;


}
