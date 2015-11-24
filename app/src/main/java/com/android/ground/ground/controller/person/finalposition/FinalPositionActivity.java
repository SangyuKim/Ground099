package com.android.ground.ground.controller.person.finalposition;

import android.content.Intent;
import android.graphics.Bitmap;
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

import com.android.ground.ground.R;
import com.android.ground.ground.custom.CustomToolbar;

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
        captureView = findViewById(R.id.captureView);

            gridLayout = (GridLayout)findViewById(R.id.gridLayout_field);



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
