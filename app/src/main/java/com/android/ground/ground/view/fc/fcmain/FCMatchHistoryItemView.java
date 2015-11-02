package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.widget.FrameLayout;

import com.android.ground.ground.R;
import com.android.ground.ground.model.fc.fcmain.FCMatchHistoryListItem;
import com.android.ground.ground.model.fc.fcmain.FCMemberListItem;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class FCMatchHistoryItemView extends FrameLayout {
    public FCMatchHistoryItemView(Context context) {
        super(context);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.custom_controller_fc_fcmain_matchhistory_list, this);
    }
    public void setFCMatchHistoryListItem(FCMatchHistoryListItem item) {
    }

}
