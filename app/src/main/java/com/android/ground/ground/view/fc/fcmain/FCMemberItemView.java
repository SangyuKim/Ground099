package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.profile.MyProfileActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.Utils;
import com.android.ground.ground.model.fc.fcmain.ClubAndMember.ClubAndMemberResult;
import com.android.ground.ground.view.OnProfileClickListener;
import com.android.ground.ground.view.OnReplyClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class FCMemberItemView extends FrameLayout {

    TextView memName, skill;
    ImageView memImage, position, managerYN;

    DisplayImageOptions options;
    public ClubAndMemberResult mItem;


    public Button mBtn1;
    public Button mBtn2;
    public FCMemberItemView(Context context) {
        super(context);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.custom_controller_fc_fcmain_list, this);
        memName = (TextView)findViewById(R.id.memName);
        skill = (TextView)findViewById(R.id.skill);

        memImage = (ImageView)findViewById(R.id.memImage);
        position =  (ImageView)findViewById(R.id.position);
        managerYN = (ImageView)findViewById(R.id.managerYN);

        //프로필
        mBtn1 = (Button)findViewById(R.id.button25);
        mBtn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProfileListener != null) {
                    mProfileListener.onProfileClick(FCMemberItemView.this);
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


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(50))
                .build();
    }
    public void setFCMemberListItem(ClubAndMemberResult item) {
        mItem = item;
        memName.setText(item.memName);
        skill.setText(Double.toString(item.skill));


        ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.memImage), memImage, options);
        //position 별로 할당된 이미지 출력
        if(item.position >0 && item.position<15)
            position.setImageResource(Utils.POSITIONS[item.position-1]);

        if(item.managerYN ==0){
            managerYN.setImageResource(R.drawable.icon201);
        }else{
            managerYN.setImageResource(R.drawable.captain);
        }
        position.setImageResource(Utils.POSITIONS[item.position]);


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
