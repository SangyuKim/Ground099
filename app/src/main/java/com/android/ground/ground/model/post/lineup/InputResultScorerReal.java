package com.android.ground.ground.model.post.lineup;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-12-02.
 */
public class InputResultScorerReal {
    public int match_id;
    public int club_id;
    public int member_id;
    @SerializedName("realScorer")
    public ArrayList<InputResultScorerRealrealScorer> itemsrealScorer;
    @SerializedName("virScorer")
    public ArrayList<InputResultScorerRealvirScorer> itemsvirScorer;

}
