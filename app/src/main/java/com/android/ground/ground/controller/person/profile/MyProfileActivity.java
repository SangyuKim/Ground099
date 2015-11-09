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
import android.widget.ImageView;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.person.message.CustomDialogMessageFragment;
import com.android.ground.ground.controller.person.message.MyMessageActivity;
import com.android.ground.ground.model.Profile;

public class MyProfileActivity extends AppCompatActivity implements Profile {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btn;

        btn =  (Button)findViewById(R.id.custom_search_bar_button_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, FCActivity.class);
                startActivity(intent);
            }
        });
        btn =  (Button)findViewById(R.id.button8);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, MyMessageActivity.class);
                startActivity(intent);

            }
        });
        btn =  (Button)findViewById(R.id.button9);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, MyProfileManagementActivity.class);
                startActivity(intent);

            }
        });
        //메시지 보내기
        btn = (Button)findViewById(R.id.button41);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogMessageFragment dialog = new CustomDialogMessageFragment();
                dialog.show(getSupportFragmentManager(), "dialog");
           }
        });

        btn =  (Button)findViewById(R.id.button10);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
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
        ImageView imageView = (ImageView)findViewById(R.id.imageView12);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, FCActivity.class);
                startActivity(intent);
            }
        });
        imageView = (ImageView)findViewById(R.id.imageView13);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, FCActivity.class);
                startActivity(intent);
            }
        });
        imageView = (ImageView)findViewById(R.id.imageView14);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, FCActivity.class);
                startActivity(intent);
            }
        });
        setTitle("프로필");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
