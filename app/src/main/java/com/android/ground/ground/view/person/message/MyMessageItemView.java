package com.android.ground.ground.view.person.message;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCFragment;
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.person.message.MyMessageItem;
import com.android.ground.ground.view.OnNoClickListener;
import com.android.ground.ground.view.OnProfileClickListener;
import com.android.ground.ground.view.OnReplyClickListener;
import com.android.ground.ground.view.OnYesClickListener;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class MyMessageItemView extends FrameLayout {
    public MyMessageItemView(Context context) {
        super(context);
        init();
    }
    public void init(){
        inflate(getContext(), R.layout.custom_controller_person_message1, this);
        Button btn = (Button)findViewById(R.id.button29);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProfileListener != null) {
                    mProfileListener.onProfileClick(MyMessageItemView.this, new FCFragment());
                }
            }
        });
        btn = (Button)findViewById(R.id.button32);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReplyListener != null) {
                    mReplyListener.onReplyClick(MyMessageItemView.this);
                }
            }
        });
        btn = (Button)findViewById(R.id.button30);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoListener != null) {
                    mNoListener.onNoClick(MyMessageItemView.this);
                }
            }
        });
        btn =(Button)findViewById(R.id.button31);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mYesListener != null) {
                    mYesListener.onYesClick(MyMessageItemView.this);
                }
            }
        });

    }
    public void setMyMessageItem(MyMessageItem item){

    }


    OnProfileClickListener mProfileListener;
    public void setOnProfileListener(OnProfileClickListener listener) {
        mProfileListener = listener;
    }
    OnReplyClickListener mReplyListener;
    public void setOnReplyListener(OnReplyClickListener listener) {
        mReplyListener = listener;
    }

    OnYesClickListener mYesListener;
    public void setOnYesListener(OnYesClickListener listener) {
        mYesListener = listener;
    }

    OnNoClickListener mNoListener;
    public void setOnNoListener(OnNoClickListener listener) {
        mNoListener = listener;
    }
}
