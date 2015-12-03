package com.android.ground.ground.controller.fc.management;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.custom.CustomToolbar;

public class FCManagementActivity extends AppCompatActivity {

    TabHost tabHost;
    ViewPager pager;
    public FCManagemnetTabsAdapter mAdapter;
    CustomToolbar customToolbar;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmanagement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        customToolbar = new CustomToolbar(FCManagementActivity.this);
        try{
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(customToolbar, params);
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon401);


        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        pager = (ViewPager)findViewById(R.id.pager);

        mAdapter = new FCManagemnetTabsAdapter(FCManagementActivity.this, getSupportFragmentManager(), tabHost, pager);

        mAdapter.addTab(tabHost.newTabSpec("tab1").setIndicator("클럽 메신저"), FragmentClubMessageEdit.class, null);
        mAdapter.addTab(tabHost.newTabSpec("tab2").setIndicator("멤버 관리"), FragmentManagementMember.class, null);
        mAdapter.addTab(tabHost.newTabSpec("tab3").setIndicator("기본설정"), FragmentFCProfile.class, null);

        if(tabHost.getCurrentTab()==0){
            setTitle("클럽 메신저");
            TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
            tv.setTextColor(Color.parseColor("#ffffff"));
        }
        mAdapter.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                                             @Override
                                             public void onTabChanged(String tabId) {
                                                 for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                                                     TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                                                     tv.setTextColor(Color.parseColor("#ffc0c0c0"));
                                                 }
                                                 switch (tabId) {
                                                     case "tab1": {
                                                         setTitle("클럽 메신저");
                                                         TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                                                         tv.setTextColor(Color.parseColor("#ffffff"));
                                                     }
                                                     case "tab2": {
                                                         setTitle("멤버 관리");
                                                         TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                                                         tv.setTextColor(Color.parseColor("#ffffff"));
                                                     }
                                                     case "tab3": {
                                                         setTitle("기본설정");
                                                         TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                                                         tv.setTextColor(Color.parseColor("#ffffff"));
                                                     }

                                                 }

                                             }
                                         }
        );



        if (savedInstanceState != null) {
            tabHost.setCurrentTab(savedInstanceState.getInt("tabIndex"));
            mAdapter.onRestoreInstanceState(savedInstanceState);
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
