package com.android.ground.ground.model.naver;

import com.begentgroup.xmlparser.SerializedName;

import java.util.ArrayList;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class NaverMovies {
    public String title;
    public  String link;
    public int total;
    public   int start;
    public  int display;
    @SerializedName("item")
    public ArrayList<MovieItem> items;
}
