package com.android.ground.ground.controller.fc.fcmain;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.main.MainFragment;
import com.android.ground.ground.model.Profile;

public class FCActivity extends AppCompatActivity  implements Profile {

    public static final String TAG_FC_ACTIVITY = "1";
    int clubId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        clubId=  getIntent().getIntExtra("clubId", -1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, FCFragment.newInstance("", ""), TAG_FC_ACTIVITY).commit();
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
}
