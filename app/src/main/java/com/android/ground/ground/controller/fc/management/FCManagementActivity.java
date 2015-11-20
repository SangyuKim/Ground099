package com.android.ground.ground.controller.fc.management;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;

import com.android.ground.ground.R;

public class FCManagementActivity extends AppCompatActivity {

    TabHost tabHost;
    ViewPager pager;
    FCManagemnetTabsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmanagement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Inflate the layout for this fragment

        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        pager = (ViewPager)findViewById(R.id.pager);

        mAdapter = new FCManagemnetTabsAdapter(FCManagementActivity.this, getSupportFragmentManager(), tabHost, pager);

        mAdapter.addTab(tabHost.newTabSpec("tab1").setIndicator("클럽 메신저"), FragmentClubMessage.class, null);
        mAdapter.addTab(tabHost.newTabSpec("tab2").setIndicator("멤버 관리"), FragmentManagementMember.class, null);
        mAdapter.addTab(tabHost.newTabSpec("tab3").setIndicator("기본설정"), FragmentFCProfile.class, null);

        if(tabHost.getCurrentTab()==0){
            setTitle("클럽 메신저");
        }
        mAdapter.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                                             @Override
                                             public void onTabChanged(String tabId) {
                                                 if (tabId.equals("tab1")) {
                                                     setTitle("클럽 메신저");
                                                 } else if (tabId.equals("tab2")) {
                                                     setTitle("멤버 관리");
                                                 } else if (tabId.equals("tab3")) {
                                                     setTitle("기본설정");
                                                 }


                                             }
                                         }
        );

        if (savedInstanceState != null) {
            tabHost.setCurrentTab(savedInstanceState.getInt("tabIndex"));
            mAdapter.onRestoreInstanceState(savedInstanceState);
        }

    }

}
