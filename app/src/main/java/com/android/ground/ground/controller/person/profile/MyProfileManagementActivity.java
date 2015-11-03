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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.login.AreaSearchActivity;
import com.android.ground.ground.controller.person.main.MainActivity;

import java.io.File;

public class MyProfileManagementActivity extends AppCompatActivity {
    ImageView imageView;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button btn = (Button)findViewById(R.id.button_complete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Toast.makeText(MyProfileManagementActivity.this, "FC 탈퇴 ", Toast.LENGTH_SHORT).show();
            }
        });
        imageView = (ImageView)findViewById(R.id.imageView_my_profile);
        imageView.setOnClickListener(new View.OnClickListener() {
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


        if (savedInstanceState != null) {
            String file = savedInstanceState.getString("filename");
            if (file != null) {
                mSavedFile = new File(file);
            }
        }
    }//onCreate
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

}
