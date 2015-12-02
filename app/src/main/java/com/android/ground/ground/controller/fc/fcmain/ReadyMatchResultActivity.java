package com.android.ground.ground.controller.fc.fcmain;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresPermission;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.ReadymatchResult.MyReadyMatchSpinnerAdapter;
import com.android.ground.ground.controller.fc.fcmain.ReadymatchResult.ReadyMatchResultAdapter;
import com.android.ground.ground.controller.fc.fcmain.ReadymatchResult.ScorerGridViewAdapter;
import com.android.ground.ground.controller.person.finalposition.GridItemView2;
import com.android.ground.ground.controller.person.finalposition.ListItemView;
import com.android.ground.ground.controller.person.main.MySpinnerAdapter;
import com.android.ground.ground.custom.CustomDragDropListView;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.etc.EtcData;
import com.android.ground.ground.model.lineup.match.LineupMatch;
import com.android.ground.ground.model.lineup.match.LineupMatchResult;
import com.android.ground.ground.model.lineup.planLoc.LineupPlanLoc;
import com.android.ground.ground.model.lineup.planLoc.LineupPlanLocResult;
import com.android.ground.ground.model.lineup.result.LineupResult;
import com.android.ground.ground.model.lineup.result.LineupResultResult;
import com.android.ground.ground.model.lineup.scorer.LineupScorer;
import com.android.ground.ground.model.lineup.virtual.fomation.LineupVirtualFomation;
import com.android.ground.ground.model.lineup.virtual.fomation.LineupVirtualFomationResult;
import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualRes;
import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualResResult;
import com.android.ground.ground.model.person.main.matchinfo.matchFormation.MatchFormation;
import com.android.ground.ground.model.person.main.matchinfo.matchFormation.MatchFormationResult;
import com.android.ground.ground.model.post.lineup.LineupPlan;
import com.android.ground.ground.model.post.lineup.LineupVirtualFomationPlayerPost;
import com.android.ground.ground.model.post.lineup.LineupVirtualFomationPost;
import com.android.ground.ground.view.OnAdapterCustomTouchListener;
import com.android.ground.ground.view.OnCustomTouchListener;
import com.android.ground.ground.view.fc.fcmain.ReadyMatchResultListItemView;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadyMatchResultActivity extends AppCompatActivity implements
        CustomDragDropListView.DragListener, CustomDragDropListView.DropListener{

    GridView gridView, gridView2;

    Button btnCreateVirtual, btnComplete;
    EditText editTextVirtual;
    ReadyMatchResultListItemView matchResultListItemView;
    ScorerGridViewAdapter mScorerGridViewAdapter;

    int w, h, countInField =0;

    TextView matchDate, awayClubName, homeClubName
            , startTime, matchLocation, homeAwayPlan, homeScore, awayScore, memName
            , MVPmemName;
    int matchId, clubId, groupPosition;
    Spinner spinner;
    MyReadyMatchSpinnerAdapter mySpinnerAdapter;

    List<String> listScorer = new ArrayList<String>();

    ListView lineupVirtualRes;
    ReadyMatchResultAdapter mReadyMatchResultAdapter;

    GridLayout gridLayout;
    ListItemView listItemView;
    GridItemView2 gridItemView;
    ArrayAdapter<String> mAdapter;
    LinearLayout linearLayout;

    LineupMatchResult mLineupMatchResult;
    LineupPlanLocResult mLineupPlanLocResult;
    LineupResultResult mLineupResultResult;

    private ShareActionProvider mShareActionProvider;
    View captureView;
    ScrollView mScrollView;

    LineupPlan mLineupPlan;
    ArrayList<LineupVirtualFomationPlayerPost> mlistsLineupVirtualFomationPlayerPostReal = new ArrayList<LineupVirtualFomationPlayerPost>();
    ArrayList<LineupVirtualFomationPlayerPost> mlistsLineupVirtualFomationPlayerPostVir = new ArrayList<LineupVirtualFomationPlayerPost>();
    LineupVirtualFomationPost mlineupVirtualFomationPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_match_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        captureView = findViewById(R.id.captureView);

        matchId = getIntent().getIntExtra("matchId", -1);
        clubId = getIntent().getIntExtra("clubId", -1);
        groupPosition = getIntent().getIntExtra("groupPosition",-1);

        matchDate = (TextView)findViewById(R.id.matchDate);
//        matchDay = (TextView)findViewById(R.id.matchDay);
        awayClubName = (TextView)findViewById(R.id.awayClubName);
        homeClubName = (TextView)findViewById(R.id.homeClubName);
        startTime = (TextView)findViewById(R.id.startTime);

        matchLocation = (TextView)findViewById(R.id.matchLocation);
        homeAwayPlan = (TextView)findViewById(R.id.homeAwayPlan);

        homeScore = (TextView)findViewById(R.id.homeScore);
        awayScore = (TextView)findViewById(R.id.awayScore);
        memName = (TextView)findViewById(R.id.memName);
        MVPmemName= (TextView)findViewById(R.id.MVPmemName);
        mScrollView = (ScrollView)findViewById(R.id.scrollView3);
        btnCreateVirtual = (Button)findViewById(R.id.button22);
        editTextVirtual = (EditText)findViewById(R.id.editTextVirtual);
        btnComplete= (Button)findViewById(R.id.button48);

        matchResultListItemView = (ReadyMatchResultListItemView)findViewById(R.id.virtualPlayer);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        gridView = (GridView)findViewById(R.id.gridView);
        gridView2 = (GridView)findViewById(R.id.gridView2);

        lineupVirtualRes = (ListView)findViewById(R.id.lineupVirtualRes);
        mReadyMatchResultAdapter = new ReadyMatchResultAdapter();
        mLineupPlan = new LineupPlan();
        mlineupVirtualFomationPost =new LineupVirtualFomationPost();
        mScorerGridViewAdapter = new ScorerGridViewAdapter();
//        mReadyMatchResultAdapter.setOnAdapterCustomTouchListener(new OnAdapterCustomTouchListener() {
//            @Override
//            public void onTouch(View view, LineupVirtualResResult mItem) {
//                Log.d("hello", " list touched");
//                String text = mItem.memName;
//                ReadyMatchResultListItemView tempView = new ReadyMatchResultListItemView(ReadyMatchResultActivity.this);
//                tempView.setReadyMatchResultListItem(mItem);
//                ClipData.Item item = new ClipData.Item(text);
//                ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
//                view.startDrag(data, new View.DragShadowBuilder(view), null, 3);
//            }
//        });
        lineupVirtualRes.setAdapter(mReadyMatchResultAdapter);
        lineupVirtualRes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        if(matchId != -1){
            searchLineupMatch(matchId);
            searchLineupPlanLoc(matchId);
        }

        if(clubId != -1 && matchId != -1){
            Log.d("hello", "club id: " + clubId + "  //  " + "matchid: " + matchId);
            searchLineupVirtualRes(clubId, matchId);
        }
        if(groupPosition==0 && clubId != -1 && matchId != -1){
            setTitle("예정된 경기");

        }

        if(groupPosition !=0 &&clubId != -1 && matchId != -1){
            searchLineupResult(matchId);
            searchLineupScorer(clubId,matchId);
            if(groupPosition==1){
                setTitle("입력 대기중인 경기");
            }else if(groupPosition ==2){
                setTitle("마무리된 경기");
            }

        }


        lineupVirtualRes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();

                String text = ((ReadyMatchResultListItemView) view).memName.getText().toString();
                intent.putExtra("text", text);
                intent.putExtra("member_id", ((ReadyMatchResultListItemView) view).mItem.member_id);
                ClipData.Item item = new ClipData.Item(intent);
                ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                view.startDrag(data, new View.DragShadowBuilder(view), null, 3);
                return true;
            }
        });
        matchResultListItemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent();

                String text = ((ReadyMatchResultListItemView) v).memName.getText().toString();
                intent.putExtra("text", text);
                //가상 선수 일경우 -2
                intent.putExtra("member_id", -2);
                ClipData.Item item = new ClipData.Item(intent);
                ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                v.startDrag(data, new View.DragShadowBuilder(v), null, 3);
                return true;
            }
        });
        btnCreateVirtual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String virtualMemNmae = editTextVirtual.getText().toString();

                matchResultListItemView.setText(virtualMemNmae);


            }
        });

        setWidthHeigth();
        setSpinner();

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//===================================================라인업
                mLineupPlan.club_id = PropertyManager.getInstance().getMyPageResult().club_id;
                if (PropertyManager.getInstance().getMyPageResult().club_id == mLineupMatchResult.home_id) {
                    mLineupPlan.homeAway = 0;
                } else {
                    if (PropertyManager.getInstance().getMyPageResult().club_id == mLineupMatchResult.away_id)
                        mLineupPlan.homeAway = 1;
                }
                mLineupPlan.match_id = mLineupMatchResult.match_id;
                mLineupPlan.member_id = PropertyManager.getInstance().getUserId();

                NetworkManager.getInstance().postNetworkLineupPlan(ReadyMatchResultActivity.this, mLineupPlan, new NetworkManager.OnResultListener<EtcData>() {
                    @Override
                    public void onSuccess(EtcData result) {


                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(ReadyMatchResultActivity.this, " 라인업 등록 실패", Toast.LENGTH_SHORT).show();
                    }
                });
                for (int i = 0; i < mLinearLayouts.size(); i++) {
                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                            LineupVirtualFomationPlayerPost mLineupVirtualFomationPlayerPost = new LineupVirtualFomationPlayerPost();
                            GridItemView2 mGridItemView2 = (GridItemView2) mLinearLayouts.get(i).getChildAt(0);
                            if (mGridItemView2.member_id > 0) {
                                mLineupVirtualFomationPlayerPost.locIndex = i;
                                mLineupVirtualFomationPlayerPost.memName = mGridItemView2.getTextView().getText().toString();
                                mLineupVirtualFomationPlayerPost.member_id = mGridItemView2.member_id;
                                mlistsLineupVirtualFomationPlayerPostReal.add(mLineupVirtualFomationPlayerPost);
                            }

                        }
                    }

                }
                Gson gson = new Gson();
                String locMemInfo = gson.toJson(mlistsLineupVirtualFomationPlayerPostReal);

                mlineupVirtualFomationPost.club_id = PropertyManager.getInstance().getMyPageResult().club_id;
                mlineupVirtualFomationPost.itemslocMemInfo = mlistsLineupVirtualFomationPlayerPostReal;
                mlineupVirtualFomationPost.match_id = mLineupMatchResult.match_id;
                mlineupVirtualFomationPost.member_id = PropertyManager.getInstance().getUserId();
                NetworkManager.getInstance().postNetworkLineupVirtualFormationReal(ReadyMatchResultActivity.this, mlineupVirtualFomationPost, new NetworkManager.OnResultListener<EtcData>() {
                    @Override
                    public void onSuccess(EtcData result) {

                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(ReadyMatchResultActivity.this, " 실제선수 등록 실패", Toast.LENGTH_SHORT).show();
                    }
                });

                for (int i = 0; i < mLinearLayouts.size(); i++) {
                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                            LineupVirtualFomationPlayerPost mLineupVirtualFomationPlayerPost = new LineupVirtualFomationPlayerPost();
                            GridItemView2 mGridItemView2 = (GridItemView2) mLinearLayouts.get(i).getChildAt(0);
                            if (mGridItemView2.member_id < 0) {
                                mLineupVirtualFomationPlayerPost.locIndex = i;
                                mLineupVirtualFomationPlayerPost.memName = mGridItemView2.getTextView().getText().toString();
                                mLineupVirtualFomationPlayerPost.member_id = mGridItemView2.member_id;
                                mlistsLineupVirtualFomationPlayerPostVir.add(mLineupVirtualFomationPlayerPost);
                            }


                        }
                    }

                }
                mlineupVirtualFomationPost.club_id = PropertyManager.getInstance().getMyPageResult().club_id;
                mlineupVirtualFomationPost.itemslocVirInfo = mlistsLineupVirtualFomationPlayerPostVir;
                mlineupVirtualFomationPost.match_id = mLineupMatchResult.match_id;
                mlineupVirtualFomationPost.member_id = PropertyManager.getInstance().getUserId();
                NetworkManager.getInstance().postNetworkLineupVirtualFormationVir(ReadyMatchResultActivity.this, mlineupVirtualFomationPost, new NetworkManager.OnResultListener<EtcData>() {
                    @Override
                    public void onSuccess(EtcData result) {
                        Toast.makeText(ReadyMatchResultActivity.this, " 입력 성공", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(ReadyMatchResultActivity.this, " 가상선수 등록 실패", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


        gridView.setAdapter(mScorerGridViewAdapter);
        gridView2.setAdapter(mScorerGridViewAdapter);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        mShareActionProvider.setShareIntent(getDefaultIntent());
        return true;
    }
    private Intent getDefaultIntent(){
        makeCapture();
        String path = getCapture();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        Uri uri = Uri.parse("android.resource://"+ path);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        return intent;
    }
    private String getCapture(){
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = new File(root, "mypicture");
        File loadFIle = new File(dir, "myimage.jpeg");
        return loadFIle.getAbsolutePath();


    }
    private void makeCapture(){
        Bitmap bm = captureView(captureView);
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = new File(root, "mypicture");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File saveFile = new File(dir, "myimage.jpeg");
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Bitmap captureView(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    private void searchLineupMatch(final int matchId) {
        NetworkManager.getInstance().getNetworkLineupMatch(ReadyMatchResultActivity.this, matchId, new NetworkManager.OnResultListener<LineupMatch>() {
            @Override
            public void onSuccess(LineupMatch result) {
                for (LineupMatchResult item : result.items) {
                    mLineupMatchResult = item;
                }
                if (mLineupMatchResult != null)
                    setLineupMatchResult(mLineupMatchResult);
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    private void setLineupMatchResult(LineupMatchResult mLineupMatchResult) {
        matchDate.setText(mLineupMatchResult.matchDate);
//        matchDay.setText(Integer.toString(mLineupMatchResult.matchDay));
        awayClubName.setText(mLineupMatchResult.awayClubName);
        homeClubName.setText(mLineupMatchResult.homeClubName);
        startTime.setText(mLineupMatchResult.startTime);

    }

    private void searchLineupPlanLoc(final int matchId) {
        NetworkManager.getInstance().getNetworkLineupPlanLoc(ReadyMatchResultActivity.this, matchId, new NetworkManager.OnResultListener<LineupPlanLoc>() {
            @Override
            public void onSuccess(LineupPlanLoc result) {
                for (LineupPlanLocResult item : result.items) {
                    mLineupPlanLocResult = item;
                }
                if (mLineupPlanLocResult != null)
                    setLineupPlanLocResult(mLineupPlanLocResult);
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    private void setLineupPlanLocResult(LineupPlanLocResult mLineupPlanLocResult) {
        matchLocation.setText(mLineupPlanLocResult.matchLocation);
        if(clubId == mLineupPlanLocResult.home_id){
            homeAwayPlan.setText(Integer.toString(mLineupPlanLocResult.homePlan));
            spinner.setSelection(mLineupPlanLocResult.homePlan-1);
        } else if (clubId == mLineupPlanLocResult.away_id) {
            homeAwayPlan.setText(Integer.toString(mLineupPlanLocResult.awayPlan));
            spinner.setSelection(mLineupPlanLocResult.homePlan-1);
        }
    }
    private void setSpinner() {
        spinner = (Spinner)findViewById(R.id.spinner3);
        mySpinnerAdapter = new MyReadyMatchSpinnerAdapter(ReadyMatchResultActivity.this);
        spinner.setAdapter(mySpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLineupPlan.plan = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        initSpinnerData();
    }

    private void initSpinnerData() {

        String[] items = getResources().getStringArray(R.array.items_lineup);
        for (String s : items) {
            mySpinnerAdapter.add(s);
        }

    }

    private void searchLineupVirtualRes(final int clubId, final int matchId) {
        NetworkManager.getInstance().getNetworkLineupVirtualRes(ReadyMatchResultActivity.this, clubId, matchId, new NetworkManager.OnResultListener<LineupVirtualRes>() {
            @Override
            public void onSuccess(LineupVirtualRes result) {
                mReadyMatchResultAdapter.clear();
                for (LineupVirtualResResult item : result.items) {
                    mReadyMatchResultAdapter.add(item);
                }

            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    private void searchLineupResult(final int matchId) {
        NetworkManager.getInstance().getNetworkLineupResult(ReadyMatchResultActivity.this, matchId, new NetworkManager.OnResultListener<LineupResult>() {
            @Override
            public void onSuccess(LineupResult result) {
                for (LineupResultResult item : result.items) {
                    mLineupResultResult = item;
                }
                if (mLineupResultResult != null)
                    setLineupResultResult(mLineupResultResult);
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    private void setLineupResultResult(LineupResultResult mLineupResultResult) {
        homeScore.setText(Integer.toString(mLineupResultResult.homeScore));
        awayScore.setText(Integer.toString(mLineupResultResult.awayScore));
        MVPmemName.setText(mLineupResultResult.mvpName);
    }

    public void searchLineupVirtualFormation(int matchId, int clubId ){
        NetworkManager.getInstance().getNetworkLineupVirtualFomation(ReadyMatchResultActivity.this, clubId, matchId, new NetworkManager.OnResultListener<LineupVirtualFomation>() {
            @Override
            public void onSuccess(LineupVirtualFomation result) {
                for (LineupVirtualFomationResult item : result.items) {
                    gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                    gridItemView.setText(item.memName);
                    if(item.locIndex>0 &&item.locIndex <= 1){
                        gridItemView.setImageRes(R.drawable.lw);
                    }else if(item.locIndex <= 4){
                        gridItemView.setImageRes(R.drawable.cf);
                    }else if(item.locIndex <= 6){
                        gridItemView.setImageRes(R.drawable.rw);
                    }

                    else if(item.locIndex <= 8){
                        gridItemView.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 11){
                        gridItemView.setImageRes(R.drawable.am);
                    }else if(item.locIndex <= 13){
                        gridItemView.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 15){
                        gridItemView.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 18){
                        gridItemView.setImageRes(R.drawable.cm);
                    }else if(item.locIndex <= 20){
                        gridItemView.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 22){
                        gridItemView.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 25){
                        gridItemView.setImageRes(R.drawable.dm);
                    }else if(item.locIndex <= 27){
                        gridItemView.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 29){
                        gridItemView.setImageRes(R.drawable.lwb);
                    }else if(item.locIndex <= 32){
                        gridItemView.setImageRes(R.drawable.cb);
                    }else if(item.locIndex <= 34){
                        gridItemView.setImageRes(R.drawable.rwb);
                    }

                    else if(item.locIndex <= 36){
                        gridItemView.setImageRes(R.drawable.lb);
                    }else if(item.locIndex <= 39){
                        gridItemView.setImageRes(R.drawable.cb);
                    }else if(item.locIndex <= 41){
                        gridItemView.setImageRes(R.drawable.rb);
                    }else{
                        gridItemView.setImageRes(R.drawable.gk);
                    }
                    if (item.locIndex > 0 && item.locIndex < 50) {
                        //드래그 가능하게 하기
                        gridItemView.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                Intent intent = new Intent();

                                String text = ((GridItemView2) v).getTextView().getText().toString();
                                intent.putExtra("text", text);
                                intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                ClipData.Item item = new ClipData.Item(intent);
                                ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                v.setVisibility(View.INVISIBLE);
                                return true;
                            }
                        });
                        mLinearLayouts.get(item.locIndex+7).addView(gridItemView);
                    }

                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }
    //getNetworkLineupResultFormation
    public void searchLineupResultFormation(int matchId, int clubId){
        NetworkManager.getInstance().getNetworkLineupResultFormation(ReadyMatchResultActivity.this, matchId, clubId, new NetworkManager.OnResultListener<MatchFormation>() {
            @Override
            public void onSuccess(MatchFormation result) {
                for (MatchFormationResult item : result.items) {
                    gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                    gridItemView.setText(item.memName);
                    if(item.locIndex>0 &&item.locIndex <= 1){
                        gridItemView.setImageRes(R.drawable.lw);
                    }else if(item.locIndex <= 4){
                        gridItemView.setImageRes(R.drawable.cf);
                    }else if(item.locIndex <= 6){
                        gridItemView.setImageRes(R.drawable.rw);
                    }

                    else if(item.locIndex <= 8){
                        gridItemView.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 11){
                        gridItemView.setImageRes(R.drawable.am);
                    }else if(item.locIndex <= 13){
                        gridItemView.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 15){
                        gridItemView.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 18){
                        gridItemView.setImageRes(R.drawable.cm);
                    }else if(item.locIndex <= 20){
                        gridItemView.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 22){
                        gridItemView.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 25){
                        gridItemView.setImageRes(R.drawable.dm);
                    }else if(item.locIndex <= 27){
                        gridItemView.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 29){
                        gridItemView.setImageRes(R.drawable.lwb);
                    }else if(item.locIndex <= 32){
                        gridItemView.setImageRes(R.drawable.cb);
                    }else if(item.locIndex <= 34){
                        gridItemView.setImageRes(R.drawable.rwb);
                    }

                    else if(item.locIndex <= 36){
                        gridItemView.setImageRes(R.drawable.lb);
                    }else if(item.locIndex <= 39){
                        gridItemView.setImageRes(R.drawable.cb);
                    }else if(item.locIndex <= 41){
                        gridItemView.setImageRes(R.drawable.rb);
                    }else{
                        gridItemView.setImageRes(R.drawable.gk);
                    }
                    if (item.locIndex > 0 && item.locIndex < 50) {
                        mLinearLayouts.get(item.locIndex+7).addView(gridItemView);
                    }

                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    private void searchLineupScorer(final int clubId, final int matchId) {
        NetworkManager.getInstance().getNetworkLineupScorer(ReadyMatchResultActivity.this, clubId, matchId, new NetworkManager.OnResultListener<LineupScorer>() {
            @Override
            public void onSuccess(LineupScorer result) {
                mScorerGridViewAdapter.clear();
                for(String item: result.items){
                    mScorerGridViewAdapter.add(item);
                    Log.d("hello", item);
                }

            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    private void setLineupScorer(List<String> listScorer) {
        for(String item : listScorer){
            memName.setText(item);
        }
    }

    @Override
    public void drag(int from, int to) {
        Log.d("hello", " drag from : " + from + "/ to  : " + to);
    }

    @Override
    public void drop(int from, int to) {
        Log.d("hello", "drop from : " + from + "/ to  : " + to);
    }


    ArrayList<LinearLayout> mLinearLayouts = new ArrayList<LinearLayout>();

    int i;
    int tempInt;

    public void setWidthHeigth() {

        w= dpToPx(270)/7;
        h= dpToPx(400)/8;

        Log.d("hello", "w : " + w + "  //  h : " + h);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w,h);
        mLinearLayouts.clear();
        gridLayout.removeAllViews();

        for(i=0; i< 56; i++){
            LinearLayout mLinearLayout = new LinearLayout(ReadyMatchResultActivity.this);
            mLinearLayout.setLayoutParams(lp);
            mLinearLayouts.add(mLinearLayout);
            tempInt= i;
            if(tempInt <= 1){
                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

//                                String text = event.getClipData().getItemAt(0).getText().toString();
                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.lw);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else if(tempInt <= 4){
                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.cf);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });


            }else if(tempInt <= 6){
                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.rw);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });


            }

            else if(tempInt <= 8){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;
                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;

//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.lm);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else if(tempInt <= 11){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.am);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else if(tempInt <= 13){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;

//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.rm);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }
            else if(tempInt <= 15){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.lm);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else if(tempInt <= 18){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.cm);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else if(tempInt <= 20){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.rm);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }

            else if(tempInt <= 22){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.lm);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else if(tempInt <= 25){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;

//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.dm);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else if(tempInt <= 27){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.rm);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }

            else if(tempInt <= 29){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;

//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.lwb);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else if(tempInt <= 32){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;
                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.cb);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else if(tempInt <= 34){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.rwb);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }

            else if(tempInt <= 36){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;

//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.lb);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else if(tempInt <= 39){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;

//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.cb);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else if(tempInt<= 41){

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.rb);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }else{

                mLinearLayouts.get(i).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
                        Drawable normalShape = getResources().getDrawable(R.drawable.shape);
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
                                v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
                                v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                v.setBackgroundDrawable(normalShape);


                                // Dropped, reassign View to ViewGroup
//                            View view = (View) event.getLocalState();
//                            ViewGroup owner = (ViewGroup) view.getParent();
//                            owner.removeView(view);
//                            view.setVisibility(View.VISIBLE);


                                LinearLayout container = (LinearLayout) v;
//                            GridItemView2 container = (GridItemView2)v;

                                String text = event.getClipData().getItemAt(0).getIntent().getStringExtra("text");
                                int mMemberId = event.getClipData().getItemAt(0).getIntent().getIntExtra("member_id", -1);
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                gridItemView.setText(text);
                                gridItemView.member_id = mMemberId;
//                           gridItemView.setImageRes();
                                gridItemView.setImageRes(R.drawable.gk);


                                gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        Intent intent = new Intent();

                                        String text = ((GridItemView2) v).getTextView().getText().toString();
                                        intent.putExtra("text", text);
                                        intent.putExtra("member_id", ((GridItemView2) v).member_id);
                                        ClipData.Item item = new ClipData.Item(intent);
                                        ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                                        v.startDrag(data, new View.DragShadowBuilder(v), null, 5);
                                        v.setVisibility(View.INVISIBLE);
                                        return true;
                                    }
                                });
                                container.removeAllViews();
                                container.addView(gridItemView);


                                countInField = 0;
                                for (int i = 0; i < mLinearLayouts.size(); i++) {
                                    if (mLinearLayouts.get(i).getChildAt(0) != null) {
                                        if (mLinearLayouts.get(i).getChildAt(0).getVisibility() == View.VISIBLE) {
                                            countInField++;
                                        }
                                    }

                                }
                                if (countInField >= 12) {
                                    Toast.makeText(ReadyMatchResultActivity.this, "필드에 선수 11명이 이상이 존재합니다. ", Toast.LENGTH_SHORT).show();
                                    container.removeAllViews();
                                }

                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
                                v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;
                    }
                });

            }

            ViewGroup parent = (ViewGroup)mLinearLayouts.get(i).getParent();
            if(parent != null)
                parent.removeAllViews();
           gridLayout.addView(mLinearLayouts.get(i));


        }//for
        if(groupPosition ==0 || groupPosition ==1){
            searchLineupVirtualFormation(matchId, clubId);
        }else{
            searchLineupResultFormation(matchId, clubId);
        }
    }//windowfocus

    @Override
    protected void onPause() {
        for(int i=0; i< mLinearLayouts.size(); i++){
            mLinearLayouts.get(i).removeAllViews();
            ViewGroup parent = (ViewGroup)mLinearLayouts.get(i).getParent();
            if(parent != null)
                parent.removeAllViews();
        }
        gridLayout.removeAllViews();
        ViewGroup parent = (ViewGroup)gridLayout.getParent();
        if(parent != null){
            parent.removeAllViews();
        }
        super.onPause();
    }
    private int dpToPx(int dp)
    {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
}
