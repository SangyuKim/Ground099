package com.android.ground.ground.model.fc.fcmain;

import com.android.ground.ground.model.fc.fcmain.ClubMatchList.ClubMatchListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-06.
 */
public class FCMatchGroupItem {
    public String groupName;
    public List<ClubMatchListResult> children = new ArrayList<ClubMatchListResult>();
}
