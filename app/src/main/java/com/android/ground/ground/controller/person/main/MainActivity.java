package com.android.ground.ground.controller.person.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.etc.setting.SettingFragment;
import com.android.ground.ground.controller.fc.create.FCCreateActivity;
import com.android.ground.ground.controller.fc.fcmain.FCFragment;
import com.android.ground.ground.controller.person.message.MyMessageFragment;
import com.android.ground.ground.controller.person.profile.MyProfileFragment;
import com.android.ground.ground.model.person.main.AlarmItemData;
import com.android.ground.ground.view.person.main.AlarmItemView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private boolean isBackPressed = false;
    ListPopupWindow listPopup;
    MainAlarmAdapter mAlarmAdapter;
    Menu menu;

    public static final String TAG_MAIN_FRAGMENT = "1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listPopup = new ListPopupWindow(this);
        mAlarmAdapter = new MainAlarmAdapter();
        listPopup.setAdapter(mAlarmAdapter);
        listPopup.setAnchorView(toolbar);

//        listPopup.setContentWidth();
        listPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Fragment mFragment = (Fragment)MyMessageFragment.newInstance("","");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, mFragment)
                        .addToBackStack(null)
                        .commit();
                listPopup.dismiss();
            }
        });
        listPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                menu.findItem(R.id.action_alarm).setIcon(R.drawable.ground_alarm);
            }
        });
        listPopup.setForceIgnoreOutsideTouch(true);
        mAlarmAdapter.setOnAdapterImageListener(new MainAlarmAdapter.OnAdapterImageListener() {
            @Override
            public void onAdapterImageClick(MainAlarmAdapter adapter, AlarmItemView view, AlarmItemData data) {
                Toast.makeText(MainActivity.this, "image 선택 -> 해당 이미지 프로필로 이동", Toast.LENGTH_SHORT).show();

            }
        });
        initAlarmData();

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

    }//onCreate


//    public Fragment getActiveFragment() {
//        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//            Log.d("hello", "0 statck");
//            return null;
//        }
//        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
//        return getSupportFragmentManager().findFragmentByTag(tag);
//    }


    private void initAlarmData() {
        for(int i=0; i<5; i ++){
            AlarmItemData data = new AlarmItemData();
            data.name = "test id : " + i;
            data.content = "님이 보낸 메시지입니다. " +i;
            mAlarmAdapter.add(data);
        }
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isBackPressed &&  getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT).isVisible() && getSupportFragmentManager().getBackStackEntryCount()==0) {
                super.onBackPressed();
            }else if(!getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT).isVisible() || getSupportFragmentManager().getBackStackEntryCount() !=0){
                super.onBackPressed();
            }else{
                isBackPressed = true;
                Toast.makeText(this, "한 번 더 누르시면 종료합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_alarm) {


            if(!listPopup.isShowing()){
                listPopup.show();
                item.setIcon(R.mipmap.ic_launcher);
                return true;
            }else{
                listPopup.dismiss();
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_myprofile) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Fragment mFragment = (Fragment)MyProfileFragment.newInstance("", "");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mFragment)
                    .addToBackStack(null)
                    .commit();
            isBackPressed = false;

        } else if (id == R.id.nav_fc) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Fragment mFragment = (Fragment) FCFragment.newInstance("", "");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mFragment)
                    .addToBackStack(null)
                    .commit();
            isBackPressed = false;
        } else if (id == R.id.nav_mymessage) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Fragment mFragment = (Fragment)MyMessageFragment.newInstance("","");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mFragment)
                    .addToBackStack(null)
                    .commit();
            isBackPressed = false;
        } else if (id == R.id.nav_ground) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            isBackPressed = false;

        } else if (id == R.id.nav_fccreate) {
            Intent intent = new Intent(MainActivity.this, FCCreateActivity.class);
            startActivity(intent);
            isBackPressed = false;
         } else if (id == R.id.nav_setting) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Fragment mFragment = (Fragment) SettingFragment.newInstance("", "");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, mFragment)
                    .addToBackStack(null)
                    .commit();
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



}
