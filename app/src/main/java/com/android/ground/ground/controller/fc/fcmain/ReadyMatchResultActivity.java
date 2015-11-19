package com.android.ground.ground.controller.fc.fcmain;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.ReadymatchResult.ReadyMatchResultAdapter;
import com.android.ground.ground.controller.person.finalposition.GridItemView2;
import com.android.ground.ground.controller.person.finalposition.ListItemView;
import com.android.ground.ground.custom.CustomDragDropListView;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.lineup.match.LineupMatch;
import com.android.ground.ground.model.lineup.match.LineupMatchResult;
import com.android.ground.ground.model.lineup.planLoc.LineupPlanLoc;
import com.android.ground.ground.model.lineup.planLoc.LineupPlanLocResult;
import com.android.ground.ground.model.lineup.result.LineupResult;
import com.android.ground.ground.model.lineup.result.LineupResultResult;
import com.android.ground.ground.model.lineup.scorer.LineupScorer;
import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualRes;
import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualResResult;
import com.android.ground.ground.view.OnAdapterCustomTouchListener;
import com.android.ground.ground.view.OnCustomTouchListener;
import com.android.ground.ground.view.fc.fcmain.ReadyMatchResultListItemView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadyMatchResultActivity extends AppCompatActivity implements
        CustomDragDropListView.DragListener, CustomDragDropListView.DropListener{

    ArrayList<LinearLayout> mLinearLayouts = new ArrayList<LinearLayout>();

    int w, h, countInField =0;

    TextView matchDate, matchDay, awayClubName, homeClubName
            , startTime, matchLocation, homeAwayPlan, homeScore, awayScore, memName
            , MVPmemName;
    int matchId, clubId, groupPosition;

    List<String> listScorer = new ArrayList<String>();

    CustomDragDropListView lineupVirtualRes;
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
        matchDay = (TextView)findViewById(R.id.matchDay);
        awayClubName = (TextView)findViewById(R.id.awayClubName);
        homeClubName = (TextView)findViewById(R.id.homeClubName);
        startTime = (TextView)findViewById(R.id.startTime);

        matchLocation = (TextView)findViewById(R.id.matchLocation);
        homeAwayPlan = (TextView)findViewById(R.id.homeAwayPlan);

        homeScore = (TextView)findViewById(R.id.homeScore);
        awayScore = (TextView)findViewById(R.id.awayScore);
        memName = (TextView)findViewById(R.id.memName);
        MVPmemName= (TextView)findViewById(R.id.MVPmemName);


        gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        listItemView = (ListItemView) findViewById(R.id.listItemView);

        lineupVirtualRes = (CustomDragDropListView)findViewById(R.id.lineupVirtualRes);
        mReadyMatchResultAdapter = new ReadyMatchResultAdapter();
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


//        listItemView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                String text = ((ListItemView) v).getTextView().toString();
//                ClipData.Item item = new ClipData.Item(text);
//                ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
//                v.startDrag(data, new View.DragShadowBuilder(v), null, 1);
//                return true;
//            }
//        });

        lineupVirtualRes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String text = ((ReadyMatchResultListItemView) view).memName.getText().toString();
                Log.d("hello", text);
                ClipData.Item item = new ClipData.Item(text);
                ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                view.startDrag(data, new View.DragShadowBuilder(view), null, 3);
                return true;
            }
        });

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
        matchDay.setText(Integer.toString(mLineupMatchResult.matchDay));
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
        } else if (clubId == mLineupPlanLocResult.away_id) {
            homeAwayPlan.setText(Integer.toString(mLineupPlanLocResult.awayPlan));
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

    private void searchLineupScorer(final int clubId, final int matchId) {
        NetworkManager.getInstance().getNetworkLineupScorer(ReadyMatchResultActivity.this, clubId, matchId, new NetworkManager.OnResultListener<LineupScorer>() {
            @Override
            public void onSuccess(LineupScorer result) {
                listScorer = result.items;

                if (listScorer != null)
                    setLineupScorer(listScorer);
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    private void setLineupScorer(List<String> listScorer) {
        for(String item : listScorer){
            memName.setText(item);
            Log.d("hello", item);
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


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);

        w= gridLayout.getWidth()/7;
        h= gridLayout.getHeight()/8;

        Log.d("hello", "w : " + w + "  //  h : " + h);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w,h);

        for(int i=0; i< 56; i++){
            LinearLayout mLinearLayout = new LinearLayout(ReadyMatchResultActivity.this);
            mLinearLayout.setLayoutParams(lp);
            mLinearLayouts.add(mLinearLayout);
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

                            String text = event.getClipData().getItemAt(0).getText().toString();
                            gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                            gridItemView.setText(text);
                            gridItemView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    String text = ((GridItemView2) v).getTextView().getText().toString();
                                    ClipData.Item item = new ClipData.Item(text);
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
            ViewGroup parent = (ViewGroup)mLinearLayouts.get(i).getParent();
            if(parent != null)
                parent.removeAllViews();
            parent = (ViewGroup)gridLayout.getParent();
            if(parent != null){
                parent.removeAllViews();
            }
            gridLayout.addView(mLinearLayouts.get(i));


        }//for


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
}
