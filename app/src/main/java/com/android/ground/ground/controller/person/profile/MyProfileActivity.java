package com.android.ground.ground.controller.person.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.person.message.MyMessageActivity;
import com.android.ground.ground.custom.CustomRoundCornerProgressBar;
import com.android.ground.ground.custom.CustomToolbar;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.Utils;
import com.android.ground.ground.model.person.profile.MyPageResult;
import com.android.ground.ground.model.person.profile.MyPageTransResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MyProfileActivity extends AppCompatActivity implements Profile {

    FrameLayout profileFrame;

    TextView memNameGender, memIntro, winLoseDraw, score, mvp, skill, clubName
    ,age, memLocationName, oldClubName1,oldClubName2,oldClubName3, gender;
    ImageView memImage, oldClubImage1, oldClubImage2, oldClubImage3, clubImage
            , position, managerYN, btnFc , btnMsg;
    Button btnMsgCollection;
    CheckBox memMainDay_Mon,memMainDay_Tue,memMainDay_Wed,memMainDay_Thu,memMainDay_Fri
            ,memMainDay_Sat,memMainDay_Sun;

    MyPageResult myPageResult;
    List<MyPageTransResult> mTransList;

    CustomRoundCornerProgressBar progressBarSkill;

    DisplayImageOptions options;
    CustomToolbar customToolbar;
    Menu menu;
    ImageView btnProfileManagement;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());
    }

    LinearLayout oldLayout1, oldLayout2, oldLayout3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        customToolbar = new CustomToolbar(MyProfileActivity.this);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(customToolbar, params);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon401);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        profileFrame = (FrameLayout)findViewById(R.id.frameLayout4);
        memNameGender =(TextView)findViewById(R.id.memNameGender);
        memIntro =(TextView)findViewById(R.id.memIntro);
        winLoseDraw = (TextView)findViewById(R.id.winLoseDraw);
        score = (TextView)findViewById(R.id.score);
        mvp = (TextView)findViewById(R.id.mvp);
        skill = (TextView)findViewById(R.id.clubSkill);
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
        gender = (TextView)findViewById(R.id.gender);

        memMainDay_Mon =(CheckBox)findViewById(R.id.memMainDay_Mon);
        memMainDay_Tue =(CheckBox)findViewById(R.id.memMainDay_Tue);
        memMainDay_Wed= (CheckBox)findViewById(R.id.memMainDay_Wed);
        memMainDay_Thu= (CheckBox)findViewById(R.id.memMainDay_Thu);
        memMainDay_Fri= (CheckBox)findViewById(R.id.memMainDay_Fri);
        memMainDay_Sat=(CheckBox)findViewById(R.id.memMainDay_Sat);
        memMainDay_Sun= (CheckBox)findViewById(R.id.memMainDay_Sun);

        oldLayout1 = (LinearLayout)findViewById(R.id.oldLayout1);
        oldLayout2 = (LinearLayout)findViewById(R.id.oldLayout2);
        oldLayout3= (LinearLayout)findViewById(R.id.oldLayout3);


        oldClubName1 = (TextView)findViewById(R.id.oldClubName1);
        oldClubName2 = (TextView)findViewById(R.id.oldClubName2);
        oldClubName3 = (TextView)findViewById(R.id.oldClubName3);
        profileFrame.bringChildToFront(position);

        progressBarSkill = (CustomRoundCornerProgressBar)findViewById(R.id.progressBarSkill);
        progressBarSkill.setMax(50);

        btnFc =  (ImageView)findViewById(R.id.btnFc);
        btnFc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, FCActivity.class);
                if (myPageResult.club_id != 0)
                    intent.putExtra("clubId", myPageResult.club_id);
                startActivity(intent);
            }
        });
        btnMsg = (ImageView)findViewById(R.id.btnMsg);
        btnMsg.setImageResource(R.drawable.send_massage_non);

        btnProfileManagement =  (ImageView)findViewById(R.id.button9);
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
                oldClubName1.setText(mList.get(0).clubName);
                oldLayout1.setVisibility(View.VISIBLE);
            }
        }
        if(mList.size()>=2){
            if(mList.get(1)!= null){
                ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + mList.get(1).clubImage), oldClubImage2, options);
                oldClubName2.setText(mList.get(1).clubName);
                oldLayout2.setVisibility(View.VISIBLE);
            }
        }
        if(mList.size()>=3){
            if(mList.get(2)!= null){
                ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + mList.get(2).clubImage), oldClubImage3, options);
                oldClubName3.setText(mList.get(2).clubName);
                oldLayout3.setVisibility(View.VISIBLE);
            }
        }

    }



    public void setMyPageResult(MyPageResult mResult){
        myPageResult = mResult;
        memNameGender.setText(mResult.memName );
//        gender.setText(" (" +mResult.age +")");
        memIntro.setText(mResult.memIntro);
        winLoseDraw.setText("승 패 : " + mResult.win+"승 " +mResult.lose+"패 "+mResult.draw+"무");
        score.setText("득 점 : " + mResult.score +"골");
        mvp.setText("MVP : " + mResult.mvp + "회");
        skill.setText(Double.toString(mResult.skill));
        progressBarSkill.setProgress((int) (mResult.skill * 10));
        clubName.setText(mResult.clubName);
        age.setText(Integer.toString(mResult.age));
        memLocationName.setText(mResult.memLocationName);



        ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + mResult.memImage), memImage, options);
        ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + mResult.clubImage), clubImage, options);
        Log.d("hello", "" + mResult.position);

        position.setVisibility(View.VISIBLE);

        if(mResult.position>0 && mResult.position<=14){
            position.setImageResource(Utils.POSITIONS[mResult.position -1]);
        }
        if(mResult.managerYN==0){
            managerYN.setImageResource(R.drawable.icon201);
        }else {
            managerYN.setImageResource(R.drawable.captain);
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
            btnFc.setImageResource(R.drawable.fcpage_non);
            btnFc.setClickable(false);
        }else{
            btnFc.setImageResource(R.drawable.fcbox501);
            btnFc.setClickable(true);
        }

    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        customToolbar.setTitle(title.toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_profile_message, menu);
        this.menu = menu;
        return true;
    }
    MenuItem messageItem;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        int id = item.getItemId();
        MyApplication.getmIMM().hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken() , InputMethodManager.HIDE_NOT_ALWAYS);
        if (id == R.id.action_message) {

            messageItem = item;
//            Drawable mDrawable = getResources().getDrawable(R.drawable.massagebox);
//            mDrawable.set
            messageItem.setIcon(R.drawable.massagebox);
            Intent intent = new Intent(MyProfileActivity.this, MyMessageActivity.class);
            startActivity(intent);
            return true;
        }
        return true;
    }
}
