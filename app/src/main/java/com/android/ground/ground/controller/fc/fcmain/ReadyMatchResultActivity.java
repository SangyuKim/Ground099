package com.android.ground.ground.controller.fc.fcmain;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.finalposition.GridItemView2;
import com.android.ground.ground.controller.person.finalposition.ListItemView;
import com.android.ground.ground.controller.person.finalposition.Main24Activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReadyMatchResultActivity extends AppCompatActivity {
    GridLayout gridLayout;
    ListItemView listItemView;
    GridItemView2 gridItemView;
    ArrayAdapter<String> mAdapter;
    LinearLayout linearLayout;

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

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        listItemView = (ListItemView) findViewById(R.id.listItemView);
//       final Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
//        final Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        int resID;
        for (int i = 0; i < 50; i++) {
            String linearLayoutID = "linearLayout" + Integer.toString(i + 1);
            Log.d("hello", linearLayoutID);
            resID = getResources().getIdentifier(linearLayoutID, "id", "com.android.ground.ground");
            if (findViewById(resID) != null) {
                Log.d("hello", "ok");
                findViewById(resID).setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View v, DragEvent event) {
                        switch (event.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                // do nothing
                                break;
                            case DragEvent.ACTION_DRAG_ENTERED:
//                            v.setBackgroundDrawable(enterShape);
                                break;
                            case DragEvent.ACTION_DRAG_EXITED:
//                            v.setBackgroundDrawable(normalShape);
                                break;
                            case DragEvent.ACTION_DROP:
                                // Dropped, reassign View to ViewGroup
                                View view = (View) event.getLocalState();
                                String text = event.getClipData().getItemAt(0).getText().toString();
                                Log.d("hello", " view : " + text);
//                    ViewGroup owner = (ViewGroup) view.getParent();
//                    owner.removeView(view);
                                LinearLayout container = (LinearLayout) v;
//                            gridItemView.setTextView(((ListItemView)view).getTextView());
                                gridItemView = new GridItemView2(ReadyMatchResultActivity.this);
                                container.addView(gridItemView);
//                            view.setVisibility(View.VISIBLE);
                                break;
                            case DragEvent.ACTION_DRAG_ENDED:
//                            v.setBackgroundDrawable(normalShape);
                            default:
                                break;
                        }
                        return true;

                    }
                });
            }
        }
        listItemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String text = ((ListItemView) v).getTextView().toString();
                ClipData.Item item = new ClipData.Item(text);
                ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                v.startDrag(data, new View.DragShadowBuilder(v), null, 0);
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

}
