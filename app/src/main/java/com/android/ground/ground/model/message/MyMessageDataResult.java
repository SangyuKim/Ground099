package com.android.ground.ground.model.message;

/**
 * Created by Tacademy on 2015-11-24.
 */
public class MyMessageDataResult {
    public int message_id;
    public int code;
    //받는이
    public int collector;
    public String collectorImage;
    public String collectorName;

    public int collectorClub;
    public String collectorClubImage;
    public String collectorClubName;


    //보낸이
    public int sender;
    public String senderImage;
    public String senderName;

    public int senderClub;
    public String senderClubImage;
    public String senderClubName;

    public String msContents;

    public int match_id;
    public String matchDate;
    public String matchLocation;
    public int matchClub_id;
    public String matchClubName;

    public int watchYN;
    public Integer accRej;

    public String messageDate;

}
