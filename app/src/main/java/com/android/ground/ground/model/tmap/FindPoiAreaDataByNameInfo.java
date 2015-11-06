package com.android.ground.ground.model.tmap;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class FindPoiAreaDataByNameInfo {
    public  String totalCnt;
    public String listCnt;
    public String contFlag;

    @SerializedName("dongInfo")
    public ArrayList<DongInfo> items;


}
