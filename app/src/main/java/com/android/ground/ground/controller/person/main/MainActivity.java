package com.android.ground.ground.controller.person.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ListPopupWindow;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.MyGcmListenerService;
import com.android.ground.ground.R;
import com.android.ground.ground.controller.etc.setting.SettingFragment;
import com.android.ground.ground.controller.fc.create.FCCreateActivity;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.person.profile.MyProfileActivity;
import com.android.ground.ground.custom.CustomDrawerLayout;
import com.android.ground.ground.custom.CustomNavigationView;
import com.android.ground.ground.custom.CustomToolbar;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageResult;
import com.android.ground.ground.model.person.profile.MyPageTrans;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity
        implements CustomNavigationView.OnNavigationItemSelectedListener,
        CustomNavigationView.OnHeaderImageClickedListener,
        CustomNavigationView.OnNavigationFooterItemSelectedListener

{

    private static final String TYPEFACE_NAME = "NotoSansKR-Regular.otf";
    private Typeface typeface = null;

    CustomToolbar customToolbar;
    CustomDrawerLayout drawer;
    private boolean isBackPressed = false;
    ListPopupWindow listPopup;
    MainAlarmAdapter mAlarmAdapter;
    Menu menu;
    ImageView imageView;

    public static final long NOT_START = -1;
    long startTime = NOT_START;

    public static final int MESSAGE_BACK_TIMEOUT = 3;
    public static final int TIME_BACK_TIMEOUT = 2000;

    public static final String TAG_MAIN_FRAGMENT = "1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//                int id = PropertyManager.getInstance().getUserId();
//                if(id ==-1 ){
//                }else{
//                    searchMyPage(id);
//                    searchMyPageTrans(id);
//                }
        loadTypeface();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                                                    ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        customToolbar = new CustomToolbar(MainActivity.this);
        try {
            getSupportActionBar().setCustomView(customToolbar, params);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        drawer = (CustomDrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.setHomeAsUpIndicator(R.drawable.icon_003);
        toggle.setDrawerIndicatorEnabled(true);

        drawer.setDrawerListener(toggle);
        drawer.setOnDrawerListener(new CustomDrawerLayout.OnDrawerListener() {
            @Override
            public void onAdapterDialogClick() {
                MyApplication.getmIMM().hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        toggle.syncState();

        ActionBar mActionBar =  getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.icon_003);


        CustomNavigationView navigationView = (CustomNavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear(); //clear old inflated items.
        if (PropertyManager.getInstance().getMyPageResult().clubYN == 0) {
            navigationView.inflateMenu(R.menu.activity_main_drawer_noclub);
        }else{
            navigationView.inflateMenu(R.menu.activity_main_drawer_yesclub);
        }
        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setHeaderItemSelectedListener(this);
        navigationView.setHeaderImageClickedListener(this);
        navigationView.setOnNavigationFooterItemSelectedListener(this);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MainFragment.newInstance("",""), TAG_MAIN_FRAGMENT).commit();
        }




//            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.container, StackFragment.newInstance("base")).commit();
//            }

//            Button btn = (Button)findViewById(R.id.btn_prev);
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//                        getSupportFragmentManager().popBackStack();
//                    }
//                }
//            });
//
//            btn = (Button)findViewById(R.id.btn_next);
//            btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int count = getSupportFragmentManager().getBackStackEntryCount();
//                    if (count < list.length) {
//                        getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.container, list[count])
//                                .addToBackStack(null)
//                                .commit();
//                    } else {
//                        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    }
//                }
//            });
        searchMyPage(PropertyManager.getInstance().getUserId());

    }//onCreate

    private void loadTypeface() {
        if(typeface==null)
            typeface = Typeface.createFromAsset(getAssets(), TYPEFACE_NAME);
    }
    @Override
    public void setContentView(int viewId) {
        View view = LayoutInflater.from(this).inflate(viewId, null);
        ViewGroup group = (ViewGroup)view;
        int childCnt = group.getChildCount();
        for(int i=0; i<childCnt; i++){
            View v = group.getChildAt(i);
            if(v instanceof TextView){
                ((TextView)v).setTypeface(typeface);
            }
        }
        super.setContentView(view);
    }


//    public Fragment getActiveFragment() {
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            Log.d("hello", "0 statck");
//            return null;
//        }
//        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
//        return getSupportFragmentManager().findFragmentByTag(tag);
//    }





    @Override
    public void onBackPressed() {
        if(isAlarmOpened){
            alarmItem.setIcon(R.drawable.icon_002);
            isAlarmOpened = false;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isBackPressed &&  getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT).isVisible() && getSupportFragmentManager().getBackStackEntryCount()==0) {
                mHandler.removeMessages(MESSAGE_BACK_TIMEOUT);
                super.onBackPressed();
            }else if(!getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT).isVisible() || getSupportFragmentManager().getBackStackEntryCount() !=0){
                super.onBackPressed();
            }else{
                isBackPressed = true;
                Toast.makeText(this, "한 번 더 누르시면 종료합니다.", Toast.LENGTH_SHORT).show();
                mHandler.sendEmptyMessageDelayed(MESSAGE_BACK_TIMEOUT, TIME_BACK_TIMEOUT);
            }
        }
    }

    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                    case MESSAGE_BACK_TIMEOUT :
                    isBackPressed = false;
                    break;
            }
        }
    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }
    boolean isAlarmOpened = false;
    MenuItem alarmItem;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        MyApplication.getmIMM().hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken() , InputMethodManager.HIDE_NOT_ALWAYS);
        if (id == R.id.action_alarm) {
            alarmItem = item;
            if(!isAlarmOpened){
                final Fragment mFragment = AlarmFragment.newInstance("", "");

//                ((AlarmFragment)mFragment).setOnAlarmClickListener(new OnAlarmClickListener() {
//                    @Override
//                    public void onDialogClick(boolean isClicked) {
//                        alarmItem.setIcon(R.drawable.icon_002);
//                    }
//                });

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, mFragment, MyGcmListenerService.NOTI_TAG)
                        .addToBackStack(null)
                        .commit();
                isAlarmOpened = true;
                return true;
            }else{
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                item.setIcon(R.drawable.icon_002);
                isAlarmOpened = false;
                return true;
            }
//            if(!listPopup.isShowing()){
//                listPopup.show();

//                return true;
//            }else{
//                listPopup.dismiss();
//                return true;
//            }

        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
         if (id == R.id.nav_fc) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if(alarmItem != null){
                alarmItem.setIcon(R.drawable.icon_002);
                isAlarmOpened = false;
            }
            Intent intent = new Intent(MainActivity.this, FCActivity.class);
            startActivity(intent);
            isBackPressed = false;
        } else if (id == R.id.nav_ground) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if(alarmItem != null){
                alarmItem.setIcon(R.drawable.icon_002);
                isAlarmOpened = false;
            }
            isBackPressed = false;

        } else if (id == R.id.nav_fccreate) {
            Intent intent = new Intent(MainActivity.this, FCCreateActivity.class);
            startActivity(intent);
            isBackPressed = false;
         }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//    private int measureContentWidth(MainAlarmAdapter listAdapter) {
//        ViewGroup mMeasureParent = null;
//        int maxWidth = 0;
//        View itemView = null;
//        int itemType = 0;
//
//        final MainAlarmAdapter adapter = listAdapter;
//        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        final int count = adapter.getCount();
//        for (int i = 0; i < count; i++) {
//            final int positionType = adapter.getItemViewType(i);
//            if (positionType != itemType) {
//                itemType = positionType;
//                itemView = null;
//            }
//
//            if (mMeasureParent == null) {
//                mMeasureParent = new FrameLayout(MyApplication.getContext());
//            }
//
//            itemView = adapter.getView(i, itemView, mMeasureParent);
//            itemView.measure(widthMeasureSpec, heightMeasureSpec);
//
//            final int itemWidth = itemView.getMeasuredWidth();
//
//            if (itemWidth > maxWidth) {
//                maxWidth = itemWidth;
//            }
//        }
//
//        return maxWidth;
//    }


    @Override
    protected void onDestroy() {
        MyApplication.getmIMM().hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());
    }

    @Override
    protected void onPause() {
        MyApplication.getmIMM().hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken() , InputMethodManager.HIDE_NOT_ALWAYS);
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onHeaderImageClicked(View view) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if(alarmItem != null){
            alarmItem.setIcon(R.drawable.icon_002);
            isAlarmOpened = false;
        }

        Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
        startActivity(intent);
        isBackPressed = false;
    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        customToolbar.setTitle(title.toString());
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }

    @Override
    public boolean onNavigationFooterItemSelected(View view) {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if(alarmItem != null){
            alarmItem.setIcon(R.drawable.icon_002);
            isAlarmOpened = false;
        }
        Fragment mFragment =  SettingFragment.newInstance("", "");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment)
                .addToBackStack(null)
                .commit();
        isBackPressed = false;


        return true;
    }

    private void searchMyPage(final int memberId) {
//        showWaitingDialog();
        NetworkManager.getInstance().getNetworkMyPage(MainActivity.this, memberId, new NetworkManager.OnResultListener<MyPage>() {
            @Override
            public void onSuccess(MyPage result) {
//                unShowWaitingDialog();

                for (MyPageResult item : result.items) {
                    PropertyManager.getInstance().setMyPageResult(item);
                    searchMyPageTrans(memberId);
                }
            }

            @Override
            public void onFail(int code) {
//                Toast.makeText(SplashActivity.this, "선수 정보 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
//                unShowWaitingDialog();
            }
        });
    }
    private void searchMyPageTrans(final int memberId) {

//        showWaitingDialog();
        NetworkManager.getInstance().getNetworkMyPageTrans(MainActivity.this, memberId, new NetworkManager.OnResultListener<MyPageTrans>() {
            @Override
            public void onSuccess(MyPageTrans result) {
//                unShowWaitingDialog();
                Log.d("hello",  PropertyManager.getInstance().getDeviceId());
                Log.d("hello", PropertyManager.getInstance().getRegistrationToken());

                PropertyManager.getInstance().setMyPageTransResult(result.items);

            }

            @Override
            public void onFail(int code) {
//                Toast.makeText(SplashActivity.this, "선수 정보 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
//                unShowWaitingDialog();
            }
        });
    }

}
