package com.android.ground.ground.controller.fc.create;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.support.v7.widget.SwitchCompat;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.etc.Area.AreaSearchActivity;
import com.android.ground.ground.custom.CustomToolbar;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.post.fcCreate.ClubProfile;
import com.android.ground.ground.model.post.signup.UserProfile;

import java.io.File;

public class FCCreateActivity extends AppCompatActivity {


    ClubProfile mClubProfile;
    CustomToolbar customToolbar;
    Menu menu;

    TextView  textArea;
    CheckBox clubMainDay_Mon, clubMainDay_Tue,clubMainDay_Wed,clubMainDay_Thu,clubMainDay_Fri,clubMainDay_Sat,clubMainDay_Sun;
    SwitchCompat fieldYN , memYN, matchYN;
    EditText clubName, clubField, clubIntro;


    public static final int REQUEST_CODE_CROP = 0;
    File mSavedFile;
    public static final int REQ_AREA_SEARCH = 1;
    final String[] items = new String[]{"사진 앨범 ",  "카메라"};
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fccreate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mClubProfile = new ClubProfile();


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        customToolbar = new CustomToolbar(FCCreateActivity.this);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(customToolbar, params);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon401);
        }catch(Exception e){
            e.printStackTrace();
        }
        LinearLayout areaLayout = (LinearLayout)findViewById(R.id.button_area_search);


        textArea = (TextView) findViewById(R.id.textArea);
        clubMainDay_Mon = (CheckBox)findViewById(R.id.clubMainDay_Mon);
        clubMainDay_Tue = (CheckBox)findViewById(R.id.clubMainDay_Tue);
        clubMainDay_Wed = (CheckBox)findViewById(R.id.clubMainDay_Wed);
        clubMainDay_Thu = (CheckBox)findViewById(R.id.clubMainDay_Thu);
        clubMainDay_Fri = (CheckBox)findViewById(R.id.clubMainDay_Fri);
        clubMainDay_Sat = (CheckBox)findViewById(R.id.clubMainDay_Sat);
        clubMainDay_Sun = (CheckBox)findViewById(R.id.clubMainDay_Sun);

        fieldYN = (SwitchCompat)findViewById(R.id.fieldYN);
        memYN = (SwitchCompat)findViewById(R.id.memYN);
        matchYN = (SwitchCompat)findViewById(R.id.matchYN);

        clubField = (EditText)findViewById(R.id.clubField);
        clubName = (EditText)findViewById(R.id.clubName);
        clubIntro = (EditText)findViewById(R.id.clubIntro);


        areaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FCCreateActivity.this, AreaSearchActivity.class);
                startActivityForResult(intent, REQ_AREA_SEARCH);
            }
        });

        Button btn = (Button)findViewById(R.id.button17);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //유저가 사진 입력하지 않았을 때 처리하기 !!

                if(mSavedFile!=null)
                    mClubProfile.mFile =mSavedFile;

                mClubProfile.clubName= clubName.getText().toString();

                mClubProfile.clubLocationName ="서울시 관악구 연구공원";
                mClubProfile.latitude =91.0;
                mClubProfile.longitude=92.0;


                if(clubMainDay_Mon.isChecked()){
                    mClubProfile.clubMainDay_Mon=1;
                }else{
                    mClubProfile.clubMainDay_Mon=0;
                }
                if(clubMainDay_Tue.isChecked()){
                    mClubProfile.clubMainDay_Tue=1;
                }else{
                    mClubProfile.clubMainDay_Tue=0;
                }
                if(clubMainDay_Wed.isChecked()){
                    mClubProfile.clubMainDay_Wed=1;
                }else{
                    mClubProfile.clubMainDay_Wed=0;
                }
                if(clubMainDay_Thu.isChecked()){
                    mClubProfile.clubMainDay_Thu=1;
                }else{
                    mClubProfile.clubMainDay_Thu=0;
                }
                if(clubMainDay_Fri.isChecked()){
                    mClubProfile.clubMainDay_Fri=1;
                }else{
                    mClubProfile.clubMainDay_Fri=0;
                }
                if(clubMainDay_Sat.isChecked()){
                    mClubProfile.clubMainDay_Sat=1;
                }else{
                    mClubProfile.clubMainDay_Sat=0;
                }
                    if(clubMainDay_Sun.isChecked()){
                    mClubProfile.clubMainDay_Sun=1;
                }else{
                    mClubProfile.clubMainDay_Sun=0;
                }

                if(memYN.isChecked()){
                    mClubProfile.memYN =1;
                }else{
                    mClubProfile.memYN =0;
                }
                if(fieldYN.isChecked()){
                    mClubProfile.fieldYN =1;
                }else{
                    mClubProfile.fieldYN =0;
                }
                if(matchYN.isChecked()){
                    mClubProfile.matchYN =1;
                }else{
                    mClubProfile.matchYN =0;
                }

               mClubProfile.clubField =clubField.getText().toString();



                mClubProfile.clubIntro=clubIntro.getText().toString();

                NetworkManager.getInstance().postNetworkMakeClub(FCCreateActivity.this, mClubProfile);


                finish();
            }
        });
        imageView = (ImageView)findViewById(R.id.imageView_fc_profile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FCCreateActivity.this);
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
        if (savedInstanceState != null) {
            String file = savedInstanceState.getString("filename");
            if (file != null) {
                mSavedFile = new File(file);
            }
        }
        setTitle("FC창단하기");
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
            imageView.setImageBitmap(bm);
        }
        if(requestCode == REQ_AREA_SEARCH && resultCode == Activity.RESULT_OK){
            String userArea = data.getExtras().getString("userArea");
            double latitude = data.getExtras().getDouble("latitude");
            mClubProfile.latitude = latitude;
            double longitude = data.getExtras().getDouble("longitude");
            mClubProfile.longitude = longitude;
            textArea.setText(userArea);
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
