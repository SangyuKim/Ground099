package com.android.ground.ground.view.person.message;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.message.CustomDialogMessageFragment;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.etc.EtcData;
import com.android.ground.ground.model.message.ClubMessageDataResult;
import com.android.ground.ground.model.message.MyMessageDataResult;
import com.android.ground.ground.model.post.push.MatchCreateData;
import com.android.ground.ground.model.post.push.MatchCreateDataResponse;
import com.android.ground.ground.model.post.push.MatchCreateDataResponseResult;
import com.android.ground.ground.model.post.push.Push200;
import com.android.ground.ground.model.post.push.Push201Response;
import com.android.ground.ground.model.post.push.Push301;
import com.android.ground.ground.model.post.push.Push301Response;
import com.android.ground.ground.model.post.push.Push302Response;
import com.android.ground.ground.view.OnNoClickListener;
import com.android.ground.ground.view.OnProfileClickListener;
import com.android.ground.ground.view.OnReplyClickListener;
import com.android.ground.ground.view.OnYesClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class MyMessageItemViewEdit extends FrameLayout implements Checkable {


    TextView textViewName, messageDate, msContents;
    ImageView imageViewMessage ,yes, no, reply;
    public ClubMessageDataResult mClubMessageDataResult;
    public void setCheckBox_message(boolean isChecked) {
        this.checkBox_message.setChecked(isChecked);
    }
    public void setCheckBox_messageVisible(){
        checkBox_message.setVisibility(View.VISIBLE);
    }

    CheckBox checkBox_message;
    public MyMessageDataResult mItem;

    DisplayImageOptions options;

    public MyMessageItemViewEdit(Context context) {
        super(context);
        init();
    }
    public void init(){
        inflate(getContext(), R.layout.custom_controller_person_message_edit, this);
        textViewName = (TextView)findViewById(R.id.textViewName);
        messageDate = (TextView)findViewById(R.id.messageDate);
        msContents = (TextView)findViewById(R.id.msContents);
        imageViewMessage= (ImageView)findViewById(R.id.imageViewMessage);
        checkBox_message =(CheckBox)findViewById(R.id.checkBox_message);

        imageViewMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProfileListener != null) {
                    mProfileListener.onProfileClick(MyMessageItemViewEdit.this);
                }
            }
        });
        reply = (ImageView)findViewById(R.id.button32);
        reply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mReplyListener != null) {
                    mReplyListener.onReplyClick(MyMessageItemViewEdit.this);
                }


            }
        });
        no = (ImageView)findViewById(R.id.button30);
        no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNoListener != null) {
                    mNoListener.onNoClick(MyMessageItemViewEdit.this);
                }

            }
        });
        yes =(ImageView)findViewById(R.id.button31);
        yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mYesListener != null) {
                    mYesListener.onYesClick(MyMessageItemViewEdit.this);
                }
                if(mItem != null){
                    switch (mItem.code){
                        case 301:{
                            Push301Response mPush301Response = new Push301Response();
                            mPush301Response.club_id = PropertyManager.getInstance().getMyPageResult().club_id;
                            mPush301Response.match_id = mItem.match_id ;
                            mPush301Response.member_id = PropertyManager.getInstance().getUserId();
                            mPush301Response.accRej =1;
                            NetworkManager.getInstance().postNetworkMessage301Response(getContext(), mPush301Response, new NetworkManager.OnResultListener<EtcData>() {
                                @Override
                                public void onSuccess(EtcData result) {
                                    Toast.makeText(getContext(), "매치 참가 승낙하였습니다." ,Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFail(int code) {
                                    Toast.makeText(getContext(), "매치 참가 승낙 실패하였습니다." ,Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        }
                        case 302: {
                            Push302Response mPush302Response = new Push302Response();
                            mPush302Response.accRej =1;
                            mPush302Response.club_id = mItem.senderClub;
                            mPush302Response.member_id = PropertyManager.getInstance().getUserId();
                            NetworkManager.getInstance().postNetworkMessage302Response(getContext(),mPush302Response , new NetworkManager.OnResultListener<EtcData>() {
                                @Override
                                public void onSuccess(EtcData result) {
                                    Toast.makeText(getContext(), "가입 승낙하였습니다.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFail(int code) {
                                    Toast.makeText(getContext(), "가입 승낙 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }else{
                    switch (mClubMessageDataResult.code){
                        case 201:{
                            final Push201Response mPush201Response = new Push201Response();
                            mPush201Response.accRej =1 ;
                            mPush201Response.member_id = PropertyManager.getInstance().getUserId();
                            mPush201Response.club_id = mClubMessageDataResult.collectorClub;
                            mPush201Response.message_id = mClubMessageDataResult.message_id;
                            mPush201Response.sender_id =  mClubMessageDataResult.sender;
//                        mPush200.contents
                            NetworkManager.getInstance().postNetworkMessage201Response(getContext(), mPush201Response, new NetworkManager.OnResultListener<EtcData>() {
                                @Override
                                public void onSuccess(EtcData result) {
                                    Toast.makeText(getContext(), "가입 승낙하였습니다. ", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFail(int code) {
                                    Toast.makeText(getContext(), "가입 승낙 실패하였습니다. ", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        }

                        case 401: {
                            MatchCreateData matchCreateData = new MatchCreateData();
                            matchCreateData.startTime = mClubMessageDataResult.startTime;
                            matchCreateData.member_id = PropertyManager.getInstance().getUserId();
                            matchCreateData.matchLocation = mClubMessageDataResult.matchLocation;
                            matchCreateData.away_id = mClubMessageDataResult.senderClub;
                            matchCreateData.endTime = mClubMessageDataResult.endTime;
                            matchCreateData.home_id = mClubMessageDataResult.collectorClub ;
                            matchCreateData.matchDate = mClubMessageDataResult.matchDate;
                            NetworkManager.getInstance().postNetworkMatchCreate(getContext(), matchCreateData, new NetworkManager.OnResultListener<MatchCreateDataResponse>() {
                                @Override
                                public void onSuccess(MatchCreateDataResponse result) {
                                    Toast.makeText(getContext(), "매치 승낙 성공하였습니다. ", Toast.LENGTH_SHORT).show();
                                    final Push301 mPush301 = new Push301();
                                    mPush301.away_id = mClubMessageDataResult.senderClub;
                                    mPush301.home_id = mClubMessageDataResult.collectorClub;

                                    mPush301.match_id = result.result.match_id;

                                    NetworkManager.getInstance().postNetworkMessage301(getContext(), mPush301, new NetworkManager.OnResultListener<EtcData>() {
                                        @Override
                                        public void onSuccess(EtcData result) {
                                            NetworkManager.getInstance().postNetworkPush301(getContext(), mPush301, new NetworkManager.OnResultListener<EtcData>() {
                                                @Override
                                                public void onSuccess(EtcData result) {
                                                    Toast.makeText(getContext(), "매치 정보 전달을 성공하였습니다. ", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFail(int code) {
                                                    Toast.makeText(getContext(), "매치 정보 전달 푸시를 실패하였습니다. ", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFail(int code) {
                                            Toast.makeText(getContext(), "매치 정보 전달을 실패하였습니다. ", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFail(int code) {
                                    Toast.makeText(getContext(), "매치 승낙 실패하였습니다.. " ,Toast.LENGTH_SHORT).show();

                                }
                            });
                            break;
                        }
                    }
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
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 200 :{
                textViewName.setText(item.senderName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 201 :{
                textViewName.setText(item.senderName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderImage), imageViewMessage, options);
                reply.setVisibility(View.GONE);
                no.setVisibility(View.VISIBLE);
                yes.setVisibility(View.VISIBLE);
                break;
            }
            case 300 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 301 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.GONE);
                no.setVisibility(View.VISIBLE);
                yes.setVisibility(View.VISIBLE);
                break;
            }
            case 302 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.GONE);
                no.setVisibility(View.VISIBLE);
                yes.setVisibility(View.VISIBLE);
                break;
            }
            case 400 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 401 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageViewMessage, options);
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
        if(item.sender == PropertyManager.getInstance().getMyPageResult().club_id){
            reply.setVisibility(View.GONE);
            no.setVisibility(View.GONE);
            yes.setVisibility(View.GONE);
        }
        msContents.setText(item.msContents);
        messageDate.setText(item.messageDate);
        if(item.watchYN == 1){
            setBackgroundColor(getResources().getColor(R.color.gray));
        }else{
            setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }


    }

    public void setMyMessageItem(ClubMessageDataResult item){
        mClubMessageDataResult = item;
        switch (item.code){
            case 100 :{
                textViewName.setText(item.senderName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 200 :{
                textViewName.setText(item.senderName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 201 :{
                textViewName.setText(item.senderName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderImage), imageViewMessage, options);
                reply.setVisibility(View.GONE);
                no.setVisibility(View.VISIBLE);
                yes.setVisibility(View.VISIBLE);
                break;
            }
            case 300 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 301 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.GONE);
                no.setVisibility(View.VISIBLE);
                yes.setVisibility(View.VISIBLE);
                break;
            }
            case 302 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.GONE);
                no.setVisibility(View.VISIBLE);
                yes.setVisibility(View.VISIBLE);
                break;
            }
            case 400 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageViewMessage, options);
                reply.setVisibility(View.VISIBLE);
                no.setVisibility(View.GONE);
                yes.setVisibility(View.GONE);
                break;
            }
            case 401 :{
                textViewName.setText(item.senderClubName);
                ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.senderClubImage), imageViewMessage, options);
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
        if(item.sender == PropertyManager.getInstance().getUserId()){
            reply.setVisibility(View.GONE);
            no.setVisibility(View.GONE);
            yes.setVisibility(View.GONE);
        }
        msContents.setText(item.msContents);
        messageDate.setText(item.messageDate);

        if(item.watchYN == 1){
            setBackgroundColor(getResources().getColor(R.color.gray));
        }else{
            setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }


    }





    @Override
    public void setChecked(boolean checked) {
        if (checked != isChecked) {
            isChecked = checked;
            checkBox_message.setChecked(checked);
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
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
    boolean isChecked = false;




}
