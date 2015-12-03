package com.android.ground.ground.controller.person.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ground.ground.MyGcmListenerService;
import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.person.main.MainFragment;
import com.android.ground.ground.controller.person.profile.MyProfileActivity;
import com.android.ground.ground.custom.CustomToolbar;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.person.message.MyMessageItem;
import com.android.ground.ground.view.OnAdapterNoListener;
import com.android.ground.ground.view.OnAdapterProfileListener;
import com.android.ground.ground.view.OnAdapterReplyListener;
import com.android.ground.ground.view.OnAdapterYesListener;

import java.util.ArrayList;
import java.util.List;

public class MyMessageActivity extends AppCompatActivity {
    CustomToolbar customToolbar;
    Menu menu;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        customToolbar = new CustomToolbar(MyMessageActivity.this);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(customToolbar, params);


            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon401);
        }catch(Exception e){
            e.printStackTrace();
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MyMessageFragment.newInstance("", ""), MyGcmListenerService.MY_MESSAGE_TAG).commit();
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if(getSupportFragmentManager().getBackStackEntryCount()==0){
                    finish();
                }else{
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
        }
        return super.onOptionsItemSelected(item);
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
