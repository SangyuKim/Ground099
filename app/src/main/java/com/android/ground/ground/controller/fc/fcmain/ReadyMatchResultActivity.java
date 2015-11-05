package com.android.ground.ground.controller.fc.fcmain;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.finalposition.GridItemView2;
import com.android.ground.ground.controller.person.finalposition.ListItemView;
import com.android.ground.ground.controller.person.finalposition.Main24Activity;

public class ReadyMatchResultActivity extends AppCompatActivity {
    GridLayout gridLayout;
    ListItemView listItemView;
    GridItemView2 gridItemView;
    ArrayAdapter<String> mAdapter;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_match_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
}
