package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.message.CustomDialogMessageFragment;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMainResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class FCMemberHeaderItemView extends FrameLayout{
    public final static String ImageUrl ="https://s3-ap-northeast-1.amazonaws.com/";
    TextView clubIntro, clubName, clubLocationName;
    ImageView clubImage;
    Button memYN;
    ClubMainResult mItem;
    Button btnMsg;
    DisplayImageOptions options;
    int clubId;

    public FCMemberHeaderItemView(Context context, int clubId) {
        super(context);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.custom_controller_fc_fcmain_list_header, this);
        clubIntro = (TextView)findViewById(R.id.clubIntro);
        clubName = (TextView)findViewById(R.id.clubName);
        clubLocationName =(TextView)findViewById(R.id.clubLocationName);

        clubImage = (ImageView)findViewById(R.id.clubImage);
        memYN = (Button)findViewById(R.id.memYN);
        btnMsg = (Button)findViewById(R.id.button23);

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
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
        ImageLoader.getInstance().displayImage((ImageUrl + items.clubImage), clubImage, options);
        if(items.memYN == 0){
            memYN.setVisibility(View.INVISIBLE);
        }
    }

}
