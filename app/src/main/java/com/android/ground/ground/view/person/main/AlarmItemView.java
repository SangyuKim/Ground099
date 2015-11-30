package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.noti.NotiDataResult;
import com.android.ground.ground.model.person.main.AlarmItemData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class AlarmItemView extends FrameLayout{
    public final static String ImageUrl ="https://s3-ap-northeast-1.amazonaws.com/";
    DisplayImageOptions options;


    ImageView imageView;
    TextView textViewName, textViewContent, notiDate;
    public NotiDataResult mNotiDataResult;

    public AlarmItemView(Context context) {
        super(context);
        init();
    }
    public void init(){
        inflate(getContext(), R.layout.custom_controller_person_main_alarm_listpopup, this);
        imageView = (ImageView)findViewById(R.id.imageView_alarm);
        textViewName = (TextView)findViewById(R.id.textView_alarm_name);
        textViewContent= (TextView)findViewById(R.id.textView_alarm_content);
        notiDate = (TextView)findViewById(R.id.notiDate);


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
    public interface OnImageClickListener {
        public void onImageClick(AlarmItemView view, AlarmItemData data);
    }

    OnImageClickListener mListener;
    public void setOnImageClickListener(OnImageClickListener listener) {
        mListener = listener;
    }

    public void setAlarmItemData(NotiDataResult item){

        mNotiDataResult = item;
        switch (item.code){
            case 100 :{
                textViewName.setText(item.senderName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderImage), imageView, options);

                break;
            }
            case 200 :{
                textViewName.setText(item.senderName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderImage), imageView, options);

                break;
            }
            case 201 :{
                textViewName.setText(item.senderName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderImage), imageView, options);

                break;
            }
            case 300 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageView, options);

                break;
            }
            case 301 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageView, options);

                break;
            }
            case 302 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageView, options);

                break;
            }
            case 400 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageView, options);

                break;
            }
            case 401 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageView, options);

                break;
            }
            case 500 :{
                textViewName.setText("시스템");
                imageView.setImageResource(R.mipmap.ic_launcher);

                break;
            }
        }

        textViewContent.setText(item.msContents);
        notiDate.setText(item.messageDate);

    }
}
