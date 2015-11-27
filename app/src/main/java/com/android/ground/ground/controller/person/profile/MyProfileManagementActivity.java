package com.android.ground.ground.controller.person.profile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.etc.Area.AreaSearchActivity;
import com.android.ground.ground.controller.person.main.MainActivity;
import com.android.ground.ground.custom.CustomToolbar;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

public class MyProfileManagementActivity extends AppCompatActivity {
    int sex;
    ImageView memImage;
    TextView age, position, skill;
    EditText memLocationName, memIntro;
    CheckBox memMainDay_Mon,memMainDay_Tue,memMainDay_Wed,memMainDay_Thu
             ,memMainDay_Fri,memMainDay_Sat,memMainDay_Sun;
    RadioGroup gender;
    RadioButton rButton, rButton2;


    DisplayImageOptions options;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());
    }

    CustomToolbar customToolbar;
    Menu menu;

    public static final int REQUEST_CODE_CROP = 0;
    File mSavedFile;
    public static final int REQ_AREA_SEARCH = 1;
    final String[] items = new String[]{"사진 앨범 ",  "카메라"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_management);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        customToolbar = new CustomToolbar(MyProfileManagementActivity.this);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(customToolbar, params);


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon401);
        }catch(NullPointerException e){
            e.printStackTrace();
        }



        age = (TextView)findViewById(R.id.age);
        position = (TextView)findViewById(R.id.position);
        skill = (TextView)findViewById(R.id.skill);

        memIntro = (EditText)findViewById(R.id.memIntro);
        memLocationName = (EditText)findViewById(R.id.memLocationName);

        memMainDay_Mon= (CheckBox)findViewById(R.id.memMainDay_Mon);
        memMainDay_Tue= (CheckBox)findViewById(R.id.memMainDay_Tue);
        memMainDay_Wed= (CheckBox)findViewById(R.id.memMainDay_Wed);
        memMainDay_Thu= (CheckBox)findViewById(R.id.memMainDay_Thu);
        memMainDay_Fri= (CheckBox)findViewById(R.id.memMainDay_Fri);
        memMainDay_Sat= (CheckBox)findViewById(R.id.memMainDay_Sat);
        memMainDay_Sun= (CheckBox)findViewById(R.id.memMainDay_Sun);



        gender = (RadioGroup)findViewById(R.id.gender);
        rButton = (RadioButton)findViewById(R.id.radioButton);
        rButton2 =(RadioButton)findViewById(R.id.radioButton2);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==0){
                    sex=0;
                }
                else{
                    sex=1;
                }
            }
        });


        Button btn = (Button)findViewById(R.id.button_complete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 2015-11-12

                //완료시 서버 전송


                //서버에서 다시 프로필 받기
                //PropertyManger 수정
                searchMyPage(1);


                Intent intent = new Intent(MyProfileManagementActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn = (Button)findViewById(R.id.button_area_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "지역 검색으로 이동", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MyProfileManagementActivity.this, AreaSearchActivity.class);
                startActivityForResult(intent, REQ_AREA_SEARCH);
            }
        });
        btn = (Button)findViewById(R.id.button5);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileManagementActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("FC탈퇴");
                builder.setMessage("FC 탈퇴하시겠습니까? ");
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
        memImage = (ImageView)findViewById(R.id.memImage);
        memImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileManagementActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("사진 선택");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent photoPickerIntent = new Intent(
                                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            photoPickerIntent.setType("image/*");
                            photoPickerIntent.putExtra("crop", "true");
                            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
                            photoPickerIntent.putExtra("outputFormat",
                                    Bitmap.CompressFormat.JPEG.toString());
                            startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP);
                        } else {
                            Intent photoPickerIntent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            photoPickerIntent.putExtra("crop", "circle");
                            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
                            photoPickerIntent.putExtra("outputFormat",
                                    Bitmap.CompressFormat.JPEG.toString());
                            startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP);
                        }


                    }
                });
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



        setMyPage();



        if (savedInstanceState != null) {
            String file = savedInstanceState.getString("filename");
            if (file != null) {
                mSavedFile = new File(file);
            }
        }
    }//onCreate

    private void setMyPage() {
        MyPageResult item = PropertyManager.getInstance().getMyPageResult();

        ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + item.memImage), memImage, options);
        age.setText(Integer.toString(item.age));
        position.setText(Integer.toString(item.position));
        skill.setText(Double.toString(item.skill));

        memLocationName.setText(item.memLocationName);
        memIntro.setText(item.memIntro);
        if(item.memMainDay_Mon==0){
            memMainDay_Mon.setChecked(false);
        }else{
            memMainDay_Mon.setChecked(true);
        }
        if(item.memMainDay_Tue==0){
            memMainDay_Tue.setChecked(false);
        }else{
            memMainDay_Tue.setChecked(true);
        }
        if(item.memMainDay_Wed==0){
            memMainDay_Wed.setChecked(false);
        }else{
            memMainDay_Wed.setChecked(true);
        }
        if(item.memMainDay_Thu==0){
            memMainDay_Thu.setChecked(false);
        }else{
            memMainDay_Thu.setChecked(true);
        }
        if(item.memMainDay_Fri==0){
            memMainDay_Fri.setChecked(false);
        }else{
            memMainDay_Fri.setChecked(true);
        }
        if(item.memMainDay_Sat==0){
            memMainDay_Sat.setChecked(false);
        }else{
            memMainDay_Sat.setChecked(true);
        }
        if(item.memMainDay_Sun==0){
            memMainDay_Sun.setChecked(false);
        }else{
            memMainDay_Sun.setChecked(true);
        }
        if(item.gender==0){
            rButton.setChecked(true);
            rButton2.setChecked(false);
        }else{
            rButton2.setChecked(true);
            rButton.setChecked(false);
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
    @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE_CROP && resultCode == Activity.RESULT_OK) {
                Bitmap bm = BitmapFactory.decodeFile(mSavedFile.getAbsolutePath());
                memImage.setImageBitmap(bm);
            }
        }
        private Uri getTempUri() {
            mSavedFile = new File(Environment.getExternalStorageDirectory(),"temp_" + System.currentTimeMillis()/1000);

            return Uri.fromFile(mSavedFile);
        }


        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            if (mSavedFile != null) {
                outState.putString("filename", mSavedFile.getAbsolutePath());
            }
        }

    private void searchMyPage(final int memberId) {
        NetworkManager.getInstance().getNetworkMyPage(MyProfileManagementActivity.this, memberId, new NetworkManager.OnResultListener<MyPage>() {
            @Override
            public void onSuccess(MyPage result) {
                for (MyPageResult item : result.items) {
                    PropertyManager.getInstance().setMyPageResult(item);
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(MyProfileManagementActivity.this, "선수 정보 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
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
