package com.android.ground.ground.model.tmap;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class DongInfo {
    public String address;
    public String resLon;
    public String resLat;

    @Override
    public String toString() {
        return resLon +"//" + address;
    }
}
