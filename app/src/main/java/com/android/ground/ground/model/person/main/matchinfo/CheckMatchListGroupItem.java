package com.android.ground.ground.model.person.main.matchinfo;

import com.android.ground.ground.model.fc.fcmain.FCMatchHistoryListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-06.
 */
public class CheckMatchListGroupItem implements CheckMatchListData {
    public String text;
    public int color;
    public List<MatchInfoResult> children = new ArrayList<MatchInfoResult>();
}
