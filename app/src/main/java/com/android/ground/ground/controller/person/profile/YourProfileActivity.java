package com.android.ground.ground.controller.person.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.person.message.CustomDialogMessageFragment;
import com.android.ground.ground.custom.CustomToolbar;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageResult;
import com.android.ground.ground.model.person.profile.MyPageTrans;
import com.android.ground.ground.model.person.profile.MyPageTransResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class YourProfileActivity extends AppCompatActivity implements Profile {

    TextView memNameGender, memIntro, winLoseDraw, score, mvp, skill, clubName
            ,age, memLocationName;
    ImageView memImage, oldClubImage1, oldClubImage2, oldClubImage3, clubImage
            , position, managerYN;
    Button btnFc, btnRequest, btnMsg;
    CheckBox memMainDay_Mon,memMainDay_Tue,memMainDay_Wed,memMainDay_Thu,memMainDay_Fri
            ,memMainDay_Sat,memMainDay_Sun;

    MyPageResult myPageResult;
    List<MyPageTransResult> mTransList;

    DisplayImageOptions options;
    CustomToolbar customToolbar;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        customToolbar = new CustomToolbar(YourProfileActivity.this);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(customToolbar, params);


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon401);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
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


        btnFc =  (Button)findViewById(R.id.btnFc);
        btnFc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourProfileActivity.this, FCActivity.class);
                if(myPageResult.club_id != 0)
                    intent.putExtra("clubId",myPageResult.club_id);
                startActivity(intent);
            }
        });
        //메시지 보내기
        btnMsg = (Button)findViewById(R.id.btnMsg);
        btnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogMessageFragment dialog = new CustomDialogMessageFragment();
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });

        btnRequest =  (Button)findViewById(R.id.btnRequest);
        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(YourProfileActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("영입하기");
                builder.setMessage("영입 신청하시겠습니까? ");
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


        oldClubImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourProfileActivity.this, FCActivity.class);
                intent.putExtra("clubId", mTransList.get(0).club_id);
                startActivity(intent);
            }
        });

        oldClubImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourProfileActivity.this, FCActivity.class);
                intent.putExtra("clubId", mTransList.get(1).club_id);
                startActivity(intent);
            }
        });

        oldClubImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(YourProfileActivity.this, FCActivity.class);
                intent.putExtra("clubId", mTransList.get(2).club_id);
                startActivity(intent);
            }
        });


        setTitle("프로필");
        int id = getIntent().getIntExtra("memberId",-1);
        Log.d("hello", "id : " + id);
        if(id==-1){
            finish();
        }
        //서치
        searchYourPage(id);
        searchYourPageTrans(id);


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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
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
        if(mResult.clubYN == 0){
            btnFc.setVisibility(View.GONE);
            btnRequest.setVisibility(View.VISIBLE);
        }else{
            btnFc.setVisibility(View.VISIBLE);
            btnRequest.setVisibility(View.GONE);
        }

    }

    private void searchYourPage(final int memberId) {
        NetworkManager.getInstance().getNetworkMyPage(YourProfileActivity.this, memberId, new NetworkManager.OnResultListener<MyPage>() {
            @Override
            public void onSuccess(MyPage result) {
                for (MyPageResult item : result.items) {
                    myPageResult = item;
                }
                if(myPageResult != null)
                    setMyPageResult(myPageResult);
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(YourProfileActivity.this, "선수 정보 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void searchYourPageTrans(final int memberId) {
        NetworkManager.getInstance().getNetworkMyPageTrans(YourProfileActivity.this, memberId, new NetworkManager.OnResultListener<MyPageTrans>() {
            @Override
            public void onSuccess(MyPageTrans result) {

                mTransList = result.items;
                setMyPageTransResults(mTransList);

            }

            @Override
            public void onFail(int code) {
                Toast.makeText(YourProfileActivity.this, "선수 정보 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        customToolbar.setTitle(title.toString());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fake_main, menu);
        this.menu = menu;
        return true;
    }

}
