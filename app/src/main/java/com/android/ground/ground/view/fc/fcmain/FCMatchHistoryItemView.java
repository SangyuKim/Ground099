package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.ground.ground.R;
import com.android.ground.ground.model.fc.fcmain.FCMatchHistoryListItem;
import com.android.ground.ground.model.fc.fcmain.FCMemberListItem;
import com.android.ground.ground.view.OnDialogClickListener;
import com.android.ground.ground.view.OnProfileClickListener;

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
        Button btn = (Button)findViewById(R.id.button27);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialogListener != null){
                    mDialogListener.onDialogClick(FCMatchHistoryItemView.this);
                }
            }
        });
    }
    public void setFCMatchHistoryListItem(FCMatchHistoryListItem item) {
    }
    OnDialogClickListener mDialogListener;
    public void setOnDialogListener(OnDialogClickListener listener) {
        mDialogListener = listener;
    }

}
