package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.ground.ground.R;
import com.android.ground.ground.model.fc.fcmain.FCMemberListItem;
import com.android.ground.ground.model.naver.MovieItem;
import com.nostra13.universalimageloader.core.ImageLoader;

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
        mBtn1 = (Button)findViewById(R.id.button25);
        mBtn2 = (Button)findViewById(R.id.button26);
    }
    public void setFCMemberListItem(FCMemberListItem item) {
    }
}
