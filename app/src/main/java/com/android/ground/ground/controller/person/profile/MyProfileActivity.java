package com.android.ground.ground.controller.person.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.person.message.CustomDialogMessageFragment;
import com.android.ground.ground.controller.person.message.MyMessageActivity;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.person.profile.MyPageResult;
import com.android.ground.ground.model.person.profile.MyPageTransResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MyProfileActivity extends AppCompatActivity implements Profile {

    TextView memNameGender, memIntro, winLoseDraw, score, mvp, skill, clubName
    ,age, memLocationName;
    ImageView memImage, oldClubImage1, oldClubImage2, oldClubImage3, clubImage
            , position, managerYN;
    Button btnFc, btnMsgCollection, btnProfileManagement;
    CheckBox memMainDay_Mon,memMainDay_Tue,memMainDay_Wed,memMainDay_Thu,memMainDay_Fri
            ,memMainDay_Sat,memMainDay_Sun;

    MyPageResult myPageResult;
    List<MyPageTransResult> mTransList;

    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        memNameGender =(TextView)findViewById(R.id.memNameGender);
        memIntro =(TextView)findViewById(R.id.memIntro);
        winLoseDraw = (TextView)findViewById(R.id.winLoseDraw);
        score = (TextView)findViewById(R.id.score);
        mvp = (TextView)findViewById(R.id.mvp);
        skill = (TextView)findViewById(R.id.skill);
        clubName = (TextView)findViewById(R.id.clubName);
        age= (TextView)findViewById(R.id.age);
        memLocationName =(TextView)findViewById(R.id.memLocationName);

        memImage= (ImageView)findViewById(R.id.memImage);
        oldClubImage1 = (ImageView)findViewById(R.id.oldClubImage1);
        oldClubImage2= (ImageView)findViewById(R.id.oldClubImage2);
        oldClubImage3 = (ImageView)findViewById(R.id.oldClubImage3);
        clubImage =(ImageView)findViewById(R.id.clubImage);
        position =(ImageView)findViewById(R.id.position);
        managerYN =(ImageView)findViewById(R.id.managerYN);

        memMainDay_Mon =(CheckBox)findViewById(R.id.memMainDay_Mon);
        memMainDay_Tue =(CheckBox)findViewById(R.id.memMainDay_Tue);
        memMainDay_Wed= (CheckBox)findViewById(R.id.memMainDay_Wed);
        memMainDay_Thu= (CheckBox)findViewById(R.id.memMainDay_Thu);
        memMainDay_Fri= (CheckBox)findViewById(R.id.memMainDay_Fri);
        memMainDay_Sat=(CheckBox)findViewById(R.id.memMainDay_Sat);
        memMainDay_Sun= (CheckBox)findViewById(R.id.memMainDay_Sun);




        btnFc =  (Button)findViewById(R.id.custom_search_bar_button_cancel);
        btnFc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, FCActivity.class);
                startActivity(intent);
            }
        });
        btnMsgCollection =  (Button)findViewById(R.id.button8);
        btnMsgCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, MyMessageActivity.class);
                startActivity(intent);

            }
        });
        btnProfileManagement =  (Button)findViewById(R.id.button9);
        btnProfileManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, MyProfileManagementActivity.class);
                startActivity(intent);

            }
        });



        oldClubImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, FCActivity.class);
                intent.putExtra("clubId", mTransList.get(0).club_id);
                startActivity(intent);
            }
        });

        oldClubImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, FCActivity.class);
                intent.putExtra("clubId", mTransList.get(1).club_id);
                startActivity(intent);
            }
        });

        oldClubImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, FCActivity.class);
                intent.putExtra("clubId", mTransList.get(2).club_id);
                startActivity(intent);
            }
        });
        setTitle("프로필");
        myPageResult = PropertyManager.getInstance().getMyPageResult();
        setMyPageResult(myPageResult);

        mTransList =PropertyManager.getInstance().getMyPageTransResult();
        setMyPageTransResults(mTransList);

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

    private void setMyPageTransResults(List<MyPageTransResult> mList) {
        mTransList = mList;
        if(mList.size()>=1){
            if(mList.get(0)!= null){
                ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + mList.get(0).clubImage), oldClubImage1, options);
                oldClubImage1.setVisibility(View.VISIBLE);
            }
        }
        if(mList.size()>=2){
            if(mList.get(1)!= null){
                ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + mList.get(1).clubImage), oldClubImage2, options);
                oldClubImage2.setVisibility(View.VISIBLE);
            }
        }
        if(mList.size()>=3){
            if(mList.get(2)!= null){
                ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + mList.get(2).clubImage), oldClubImage3, options);
                oldClubImage3.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setMyPageResult(MyPageResult mResult){
        myPageResult = mResult;
        memNameGender.setText(mResult.memName + " (" +mResult.age +")");
        memIntro.setText(mResult.memIntro);
        winLoseDraw.setText(mResult.win+"승 / " +mResult.lose+"패 / "+mResult.draw+"무");
        score.setText(mResult.score +"골");
        mvp.setText(mResult.mvp +"회");
        skill.setText(Double.toString(mResult.skill));
        clubName.setText(mResult.clubName);
        age.setText(Integer.toString(mResult.age));
        memLocationName.setText(mResult.memLocationName);



        ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + mResult.memImage), memImage, options);
        ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + mResult.clubImage), clubImage, options);
        if(mResult.position==0){
            position.setVisibility(View.INVISIBLE);
        }else{
            position.setVisibility(View.VISIBLE);
        }
        if(mResult.managerYN==0){
            managerYN.setVisibility(View.INVISIBLE);
        }else {
            managerYN.setVisibility(View.VISIBLE);
        }

        if(mResult.memMainDay_Mon ==0){
            memMainDay_Mon.setChecked(false);
        }else{
            memMainDay_Mon.setChecked(true);
        }
        if(mResult.memMainDay_Tue ==0){
            memMainDay_Tue.setChecked(false);
        }else{
            memMainDay_Tue.setChecked(true);
        }
        if(mResult.memMainDay_Wed ==0){
            memMainDay_Wed.setChecked(false);
        }else{
            memMainDay_Wed.setChecked(true);
        }
        if(mResult.memMainDay_Thu ==0){
            memMainDay_Thu.setChecked(false);
        }else{
            memMainDay_Thu.setChecked(true);
        }
        if(mResult.memMainDay_Fri ==0){
            memMainDay_Fri.setChecked(false);
        }else{
            memMainDay_Fri.setChecked(true);
        }
        if(mResult.memMainDay_Sat ==0){
            memMainDay_Sat.setChecked(false);
        }else{
            memMainDay_Sat.setChecked(true);
        }
        if(mResult.memMainDay_Sun ==0){
            memMainDay_Sun.setChecked(false);
        }else{
            memMainDay_Sun.setChecked(true);
        }
        if(mResult.clubYN==0){
            btnFc.setVisibility(View.GONE);
        }else{
            btnFc.setVisibility(View.VISIBLE);
        }

    }


}
