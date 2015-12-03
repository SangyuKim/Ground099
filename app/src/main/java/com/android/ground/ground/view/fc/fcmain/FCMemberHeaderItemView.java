package com.android.ground.ground.view.fc.fcmain;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMainResult;
import com.android.ground.ground.model.post.push.Push200;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class FCMemberHeaderItemView extends FrameLayout{
    TextView clubIntro, clubName, clubLocationName, clubSkill, clubManner, clubField
            , avgAge, clubMemCnt;
    ImageView clubImage;

    CheckBox clubMainDay_Mon, clubMainDay_Tue, clubMainDay_Wed, clubMainDay_Thu, clubMainDay_Fri
            ,clubMainDay_Sat, clubMainDay_Sun;


    ClubMainResult mItem;
    DisplayImageOptions options;
    int clubId;
    ImageView btnMsg, memYN;
    public FCMemberHeaderItemView(Context context, int clubId) {
        super(context);
        this.clubId = clubId;
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.custom_controller_fc_fcmain_list_header, this);
        clubIntro = (TextView)findViewById(R.id.clubIntro);
        clubName = (TextView)findViewById(R.id.clubName);
        clubLocationName =(TextView)findViewById(R.id.clubLocationName);

        clubImage = (ImageView)findViewById(R.id.clubImage);
        memYN = (ImageView)findViewById(R.id.memYN);
        btnMsg = (ImageView)findViewById(R.id.button23);

        clubSkill =(TextView)findViewById(R.id.clubSkill);
        clubManner =(TextView)findViewById(R.id.clubManner);
        clubField =(TextView)findViewById(R.id.clubField);
        avgAge =(TextView)findViewById(R.id.avgAge);
        clubMemCnt =(TextView)findViewById(R.id.clubMemCnt);

        clubMainDay_Mon = (CheckBox)findViewById(R.id.clubMainDay_Mon);
        clubMainDay_Tue = (CheckBox)findViewById(R.id.clubMainDay_Tue);
        clubMainDay_Wed = (CheckBox)findViewById(R.id.clubMainDay_Wed);
        clubMainDay_Thu = (CheckBox)findViewById(R.id.clubMainDay_Thu);
        clubMainDay_Fri = (CheckBox)findViewById(R.id.clubMainDay_Fri);
        clubMainDay_Sat = (CheckBox)findViewById(R.id.clubMainDay_Sat);
        clubMainDay_Sun = (CheckBox)findViewById(R.id.clubMainDay_Sun);



        btnMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogMessageFragment dialog = new CustomDialogMessageFragment();
                dialog.show(((AppCompatActivity)MyApplication.getCurrentActivity()).getSupportFragmentManager(), "dialog");
            }
        });

        memYN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("가입신청");
                builder.setMessage("가입 신청하시겠습니까? ");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        final Push200 mPush200 = new Push200();
                        mPush200.collectorClub_id = clubId;
//                        mPush200.contents;
                        mPush200.member_id = PropertyManager.getInstance().getUserId();
                        mPush200.sender_id = PropertyManager.getInstance().getUserId();
                        NetworkManager.getInstance().postNetworkMessage201(getContext(), mPush200, new NetworkManager.OnResultListener<EtcData>() {
                            @Override
                            public void onSuccess(EtcData result) {
                                NetworkManager.getInstance().postNetworkPush201(getContext(), mPush200, new NetworkManager.OnResultListener<EtcData>() {
                                    @Override
                                    public void onSuccess(EtcData result) {
                                        Toast.makeText(getContext(), "가입신청을 전송하였습니다.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onFail(int code) {
                                        Toast.makeText(getContext(), "가입신청 푸시를 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onFail(int code) {
                                Toast.makeText(getContext(), "가입신청을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });


                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();

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
    public void setFCMemberHeader(ClubMainResult items){
        mItem = items;

        clubIntro.setText(items.clubIntro);
        clubName.setText(items.clubName);
        clubLocationName.setText(items.clubLocationName);
        ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + items.clubImage), clubImage, options);
        if(items.memYN == 0){
            memYN.setVisibility(View.INVISIBLE);
        }


        clubSkill.setText(Double.toString(items.clubSkill));
        clubManner.setText(Double.toString(items.clubManner));
        clubField.setText(items.clubField);
        avgAge.setText("평균나이("+ Double.toString(items.avgAge)+"세)");
        clubMemCnt.setText(Integer.toString(items.clubMemCnt)+"명");

        if(items.clubMainDay_Mon ==0){
            clubMainDay_Mon.setChecked(false);
        }else{
            clubMainDay_Mon.setChecked(true);
        }
        if(items.clubMainDay_Tue ==0){
            clubMainDay_Tue.setChecked(false);
        }else{
            clubMainDay_Tue.setChecked(true);
        }
        if(items.clubMainDay_Wed ==0){
            clubMainDay_Wed.setChecked(false);
        }else{
            clubMainDay_Wed.setChecked(true);
        }
        if(items.clubMainDay_Thu ==0){
            clubMainDay_Thu.setChecked(false);
        }else{
            clubMainDay_Thu.setChecked(true);
        }
        if(items.clubMainDay_Fri ==0){
            clubMainDay_Fri.setChecked(false);
        }else{
            clubMainDay_Fri.setChecked(true);
        }
        if(items.clubMainDay_Sat ==0){
            clubMainDay_Sat.setChecked(false);
        }else{
            clubMainDay_Sat.setChecked(true);
        }
        if(items.clubMainDay_Sun ==0){
            clubMainDay_Sun.setChecked(false);
        }else{
            clubMainDay_Sun.setChecked(true);
        }

    }

}
