package com.android.ground.ground.model.person.main.matchinfo;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class MatchInfoResult implements CheckMatchListData {
    public int match_id;
    public String matchDate;
    //1 일요일 ~ 7 토요일
    public int matchDay;
    public String startTime;
    public int home_id;
    public String homeClubName;
    public int away_id;
    public String awayClubName;
    public int insertResultYN;
    public int homeScore;
    public int awayScore;
    public String matchLocation;
    public int matchMVP;
    public String mvpName;

    @Override
    public String toString() {
        return homeClubName;
    }
}
