package com.android.ground.ground.model.naver;

import com.android.ground.ground.model.person.main.matchinfo.CheckMatchListData;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class MovieItem implements CheckMatchListData {
    public String title;
    public String link;
    public String image;
    public String director;
    public  String actor;
    public  float userRating;

    @Override
    public String toString() {
        return title + "(" + director + ")";
    }
}
