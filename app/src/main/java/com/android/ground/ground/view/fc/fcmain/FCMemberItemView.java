package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.profile.MyProfileActivity;
import com.android.ground.ground.model.fc.fcmain.FCMemberListItem;
import com.android.ground.ground.view.OnProfileClickListener;
import com.android.ground.ground.view.OnReplyClickListener;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class FCMemberItemView extends FrameLayout {
    public Button mBtn1;
    public Button mBtn2;
    public FCMemberItemView(Context context) {
        super(context);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.custom_controller_fc_fcmain_list, this);
        //프로필
        mBtn1 = (Button)findViewById(R.id.button25);
        mBtn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProfileListener != null) {
                    mProfileListener.onProfileClick(FCMemberItemView.this, new MyProfileActivity());
                }
            }
        });
        //메시지
        mBtn2 = (Button)findViewById(R.id.button26);
        mBtn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReplyListener != null) {
                    mReplyListener.onReplyClick(FCMemberItemView.this);
                }
            }
        });
    }
    public void setFCMemberListItem(FCMemberListItem item) {
    }
    OnProfileClickListener mProfileListener;
    public void setOnProfileListener(OnProfileClickListener listener) {
        mProfileListener = listener;
    }
    OnReplyClickListener mReplyListener;
    public void setOnReplyListener(OnReplyClickListener listener) {
        mReplyListener = listener;
    }
}
