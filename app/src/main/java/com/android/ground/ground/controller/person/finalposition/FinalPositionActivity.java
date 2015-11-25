package com.android.ground.ground.controller.person.finalposition;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.custom.CustomToolbar;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.lineup.match.LineupMatch;
import com.android.ground.ground.model.lineup.match.LineupMatchResult;
import com.android.ground.ground.model.person.main.matchinfo.MatchInfoResult;
import com.android.ground.ground.model.person.main.matchinfo.matchFormation.MatchFormation;
import com.android.ground.ground.model.person.main.matchinfo.matchFormation.MatchFormationResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FinalPositionActivity extends AppCompatActivity{

    private ShareActionProvider mShareActionProvider;
    View captureView;
    CustomToolbar customToolbar;
    Menu menu;
    LineupMatchResult mLineupMatchResult;
    int match_id,home_id, away_id;
    TextView matchDate, matchDay, startTime, homeClubName, awayClubName;
    GridLayout gridLayout;
    ArrayList<LinearLayout> mLinearLayouts = new ArrayList<LinearLayout>();
    int w, h, countInField =0;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_position);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


            ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            customToolbar = new CustomToolbar(FinalPositionActivity.this);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(customToolbar, params);

            try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon401);}
            catch(Exception e){
                e.printStackTrace();
            }

            match_id = getIntent().getIntExtra("matchId", -1);
            home_id = getIntent().getIntExtra("home_id", -1);
            away_id = getIntent().getIntExtra("away_id", -1);


        captureView = findViewById(R.id.captureView);
            matchDate= (TextView)findViewById(R.id.matchDate);
            matchDay= (TextView)findViewById(R.id.matchDay);
            startTime= (TextView)findViewById(R.id.startTime);
            homeClubName= (TextView)findViewById(R.id.homeClubName);
            awayClubName= (TextView)findViewById(R.id.awayClubName);



            gridLayout = (GridLayout)findViewById(R.id.gridLayout_field);

            searchMatchInfoResult(match_id);



    }

    private void searchMatchInfoResult(final int matchId) {
        NetworkManager.getInstance().getNetworkLineupMatch(FinalPositionActivity.this,matchId , new NetworkManager.OnResultListener<LineupMatch>(){
            @Override
            public void onSuccess(LineupMatch result) {
                for(LineupMatchResult item : result.items){
                    setLineupMatchResult(item);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }

    public void setLineupMatchResult(LineupMatchResult item){
        mLineupMatchResult = item;
        matchDate.setText(item.matchDate);
        matchDay.setText(Integer.toString(item.matchDay));
        startTime.setText(item.startTime);
        homeClubName.setText(item.homeClubName);
        awayClubName.setText(item.awayClubName);
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
        } catch (Exception e) {
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
    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        customToolbar.setTitle(title.toString());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        w= gridLayout.getWidth()/7;
        h= gridLayout.getHeight()/14;

        Log.d("hello", "w : " + w + "  //  h : " + h);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w,h);
        mLinearLayouts.clear();
        gridLayout.removeAllViews();
        for(int i=0; i< 98; i++){
            LinearLayout mLinearLayout = new LinearLayout(FinalPositionActivity.this);
            mLinearLayout.setLayoutParams(lp);
            mLinearLayouts.add(mLinearLayout);
            ViewGroup parent = (ViewGroup)mLinearLayouts.get(i).getParent();
            if(parent != null)
                parent.removeAllViews();
            gridLayout.addView(mLinearLayouts.get(i));
        }//for
        searchLineupResultFormationHome(match_id, home_id);
        searchLineupResultFormationAway(match_id, away_id);

    }//windowfocus

    private void searchLineupResultFormationHome(int matchId, int homeId ) {
        NetworkManager.getInstance().getNetworkLineupResultFormation(FinalPositionActivity.this, matchId, homeId, new NetworkManager.OnResultListener<MatchFormation>() {
            @Override
            public void onSuccess(MatchFormation result) {
                for(MatchFormationResult item : result.items){
                    GridItemView2 gridItemView2 = new GridItemView2(FinalPositionActivity.this);
                    gridItemView2.setText(item.memName);
                    if(item.locIndex <= 1){
                        gridItemView2.setImageRes(R.drawable.lw);
                    }else if(item.locIndex <= 4){
                        gridItemView2.setImageRes(R.drawable.cf);
                    }else if(item.locIndex <= 6){
                        gridItemView2.setImageRes(R.drawable.rw);
                    }

                    else if(item.locIndex <= 8){
                        gridItemView2.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 11){
                        gridItemView2.setImageRes(R.drawable.am);
                    }else if(item.locIndex <= 13){
                        gridItemView2.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 15){
                        gridItemView2.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 18){
//                        gridItemView2.setImageRes(R.drawable.cm);
                    }else if(item.locIndex <= 20){
                        gridItemView2.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 22){
                        gridItemView2.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 25){
                        gridItemView2.setImageRes(R.drawable.dm);
                    }else if(item.locIndex <= 27){
                        gridItemView2.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 29){
                        gridItemView2.setImageRes(R.drawable.lwb);
                    }else if(item.locIndex <= 32){
                        gridItemView2.setImageRes(R.drawable.cb);
                    }else if(item.locIndex <= 34){
                        gridItemView2.setImageRes(R.drawable.rwb);
                    }

                    else if(item.locIndex <= 36){
                        gridItemView2.setImageRes(R.drawable.lb);
                    }else if(item.locIndex <= 39){
                        gridItemView2.setImageRes(R.drawable.cb);
                    }else if(item.locIndex <= 41){
                        gridItemView2.setImageRes(R.drawable.rb);
                    }else{
//                        gridItemView2.setImageRes(R.drawable.gk);
                    }
                    if(item.virtualFlag==1){
                        gridItemView2.setBackgroundColor(Color.GRAY);
                    }
                    if(item.locIndex < 49)
                         mLinearLayouts.get(item.locIndex + 49).addView(gridItemView2);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }
    private void searchLineupResultFormationAway(int matchId, int awayId) {
        NetworkManager.getInstance().getNetworkLineupResultFormation(FinalPositionActivity.this, matchId, awayId, new NetworkManager.OnResultListener<MatchFormation>() {
            @Override
            public void onSuccess(MatchFormation result) {
                for(MatchFormationResult item : result.items){
                    GridItemView2 gridItemView2 = new GridItemView2(FinalPositionActivity.this);
                    if(item.locIndex <= 1){
                        gridItemView2.setImageRes(R.drawable.lw);
                    }else if(item.locIndex <= 4){
                        gridItemView2.setImageRes(R.drawable.cf);
                    }else if(item.locIndex <= 6){
                        gridItemView2.setImageRes(R.drawable.rw);
                    }

                    else if(item.locIndex <= 8){
                        gridItemView2.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 11){
                        gridItemView2.setImageRes(R.drawable.am);
                    }else if(item.locIndex <= 13){
                        gridItemView2.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 15){
                        gridItemView2.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 18){
//                        gridItemView2.setImageRes(R.drawable.cm);
                    }else if(item.locIndex <= 20){
                        gridItemView2.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 22){
                        gridItemView2.setImageRes(R.drawable.lm);
                    }else if(item.locIndex <= 25){
                        gridItemView2.setImageRes(R.drawable.dm);
                    }else if(item.locIndex <= 27){
                        gridItemView2.setImageRes(R.drawable.rm);
                    }

                    else if(item.locIndex <= 29){
                        gridItemView2.setImageRes(R.drawable.lwb);
                    }else if(item.locIndex <= 32){
                        gridItemView2.setImageRes(R.drawable.cb);
                    }else if(item.locIndex <= 34){
                        gridItemView2.setImageRes(R.drawable.rwb);
                    }

                    else if(item.locIndex <= 36){
                        gridItemView2.setImageRes(R.drawable.lb);
                    }else if(item.locIndex <= 39){
                        gridItemView2.setImageRes(R.drawable.cb);
                    }else if(item.locIndex <= 41){
                        gridItemView2.setImageRes(R.drawable.rb);
                    }else{
//                        gridItemView2.setImageRes(R.drawable.gk);
                    }
                    gridItemView2.setText(item.memName);
                    if(item.virtualFlag==1){
                        gridItemView2.setBackgroundColor(Color.GRAY);
                    }
                    if(item.locIndex < 49)
                        mLinearLayouts.get(48 - item.locIndex ).addView(gridItemView2);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }



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
