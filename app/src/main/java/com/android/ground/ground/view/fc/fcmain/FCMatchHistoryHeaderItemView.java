package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.widget.FrameLayout;

import com.android.ground.ground.R;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class FCMatchHistoryHeaderItemView extends FrameLayout{
    public FCMatchHistoryHeaderItemView(Context context) {
        super(context);
        init();
    }
    public void init(){
        inflate(getContext(), R.layout.custom_controller_fc_fcmain_matchhistory_header, this);
    }
}
