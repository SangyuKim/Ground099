package com.android.ground.ground.view.person.message;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.model.message.MyMessageDataResult;
import com.android.ground.ground.model.person.message.MyMessageItem;
import com.android.ground.ground.view.OnNoClickListener;
import com.android.ground.ground.view.OnProfileClickListener;
import com.android.ground.ground.view.OnReplyClickListener;
import com.android.ground.ground.view.OnYesClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class MyMessageItemView extends FrameLayout {
    public final static String ImageUrl ="https://s3-ap-northeast-1.amazonaws.com/";
    TextView textViewName, messageDate, msContents;
    ImageView imageViewMessage;
    public MyMessageDataResult mItem;
    Button yes, no, reply;

    DisplayImageOptions options;

    public MyMessageItemView(Context context) {
        super(context);
        init();
    }
    public void init(){
        inflate(getContext(), R.layout.custom_controller_person_message1, this);
        textViewName = (TextView)findViewById(R.id.textViewName);
        messageDate = (TextView)findViewById(R.id.messageDate);
        msContents = (TextView)findViewById(R.id.msContents);
        imageViewMessage= (ImageView)findViewById(R.id.imageViewMessage);


        Button btn = (Button)findViewById(R.id.button29);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProfileListener != null) {
                    mProfileListener.onProfileClick(MyMessageItemView.this, new FCActivity());
                }
            }
        });
        reply = (Button)findViewById(R.id.button32);
        reply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReplyListener != null) {
                    mReplyListener.onReplyClick(MyMessageItemView.this);
                }
            }
        });
        no = (Button)findViewById(R.id.button30);
        no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoListener != null) {
                    mNoListener.onNoClick(MyMessageItemView.this);
                }
            }
        });
        yes =(Button)findViewById(R.id.button31);
        yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mYesListener != null) {
                    mYesListener.onYesClick(MyMessageItemView.this);
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
    public void setMyMessageItem(MyMessageDataResult item){
        mItem = item;
        switch (item.code){
            case 100 :{
                textViewName.setText(item.senderName);
                ImageLoader.getInstance().displayImage((ImageUrl + item.senderImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 200 :{
                textViewName.setText(item.senderName);
                ImageLoader.getInstance().displayImage((ImageUrl + item.senderImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 201 :{
                textViewName.setText(item.senderName);
                ImageLoader.getInstance().displayImage((ImageUrl + item.senderImage), imageViewMessage, options);
                reply.setVisibility(View.GONE);
                no.setVisibility(View.VISIBLE);
                yes.setVisibility(View.VISIBLE);
                break;
            }
            case 300 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 301 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.GONE);
                no.setVisibility(View.VISIBLE);
                yes.setVisibility(View.VISIBLE);
                break;
            }
            case 302 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.GONE);
                no.setVisibility(View.VISIBLE);
                yes.setVisibility(View.VISIBLE);
                break;
            }
            case 400 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 401 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.GONE);
                no.setVisibility(View.VISIBLE);
                yes.setVisibility(View.VISIBLE);
                break;
            }
            case 500 :{
                textViewName.setText("시스템");
               imageViewMessage.setImageResource(R.mipmap.ic_launcher);
                reply.setVisibility(View.GONE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
        }
        msContents.setText(item.msContents);
        messageDate.setText(item.messageDate);

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
