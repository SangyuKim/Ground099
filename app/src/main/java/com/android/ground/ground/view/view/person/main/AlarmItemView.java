package com.android.ground.ground.view.view.person.main;

import android.content.Context;
import android.support.v7.internal.view.menu.MenuView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.person.main.AlarmItemData;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class AlarmItemView extends FrameLayout{

    AlarmItemData mAlarmItemData;
    ImageView imageView;
    TextView textViewName, textViewContent;


    public AlarmItemView(Context context) {
        super(context);
        init();
    }
    public void init(){
        inflate(getContext(), R.layout.custom_controller_person_main_alarm_listpopup, this);
        imageView = (ImageView)findViewById(R.id.imageView_alarm);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onImageClick(AlarmItemView.this, mAlarmItemData);
                }
            }
        });
        textViewName = (TextView)findViewById(R.id.textView_alarm_name);
        textViewContent= (TextView)findViewById(R.id.textView_alarm_content);
    }
    public interface OnImageClickListener {
        public void onImageClick(AlarmItemView view, AlarmItemData data);
    }

    OnImageClickListener mListener;
    public void setOnImageClickListener(OnImageClickListener listener) {
        mListener = listener;
    }

    public void setAlarmItemData(AlarmItemData data){
        mAlarmItemData = data;
        imageView.setImageResource(R.mipmap.ic_launcher);
        textViewName.setText(mAlarmItemData.name);
        textViewContent.setText(mAlarmItemData.content);
    }
}
