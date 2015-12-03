package com.android.ground.ground.controller.fc.management;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

        View tabView = createTabView(tabHost.getContext(), R.drawable.tab_411);
        mAdapter.addTab(tabHost.newTabSpec("tab1").setIndicator(tabView), FragmentClubMessageEdit.class, null);
        tabView = createTabView(tabHost.getContext(), R.drawable.tab_412);
        mAdapter.addTab(tabHost.newTabSpec("tab2").setIndicator(tabView), FragmentManagementMember.class, null);
        tabView = createTabView(tabHost.getContext(), R.drawable.tab_413);
        mAdapter.addTab(tabHost.newTabSpec("tab3").setIndicator(tabView), FragmentFCProfile.class, null);


        if(tabHost.getCurrentTab()==0){
            for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(i).findViewById(R.id.imageView5);
                String tabID = "tab_41"+(i+1);
                int resID = getResources().getIdentifier(tabID, "drawable", "com.android.ground.ground");
                tabHostImageView.setImageResource(resID);
            }
            setTitle("클럽메신저");
            ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(0).findViewById(R.id.imageView5);
            tabHostImageView.setImageResource(R.drawable.tab_401);

        }

        mAdapter.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
//                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.BLACK); // unselected

                    ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(i).findViewById(R.id.imageView5);
                    String tabID = "tab_41"+(i+1);
                    int resID = getResources().getIdentifier(tabID, "drawable", "com.android.ground.ground");
                    tabHostImageView.setImageResource(resID);

                }
                if (tabId.equals("tab1")) {
                    setTitle("클럽메신저");
                    ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(0).findViewById(R.id.imageView5);
                    tabHostImageView.setImageResource(R.drawable.tab_401);

                } else if (tabId.equals("tab2")) {
                    setTitle("멤버관리");
                    ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(1).findViewById(R.id.imageView5);
                    tabHostImageView.setImageResource(R.drawable.tab_402);

                } else if (tabId.equals("tab3")) {
                    setTitle("FC설정");
                    ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(2).findViewById(R.id.imageView5);
                    tabHostImageView.setImageResource(R.drawable.tab_403);

                }

            }
        });

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

    private static View createTabView(final Context context, final int res) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
//        TextView tv = (TextView) view.findViewById(R.id.tabsText);
//        tv.setText(text);
        ImageView imageViewTab = (ImageView)view.findViewById(R.id.imageView5);
//        imageViewTab.setImageResource();
        return view;
    }

}
