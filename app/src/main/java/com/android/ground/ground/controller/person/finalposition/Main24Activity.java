package com.android.ground.ground.controller.person.finalposition;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.android.ground.ground.R;

public class Main24Activity extends AppCompatActivity {
    GridLayout gridLayout;
    ListItemView listItemView;
    GridItemView2 gridItemView;
    ArrayAdapter<String> mAdapter;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main24);
//        gridLayout = (GridLayout)findViewById(R.id.gridLayout);
//        listItemView= (ListItemView)findViewById(R.id.listItemView);
//       final Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
//        final Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        int resID;
//        for(int i=0 ; i < 50 ; i ++) {
//            String linearLayoutID = "linearLayout" + Integer.toString(i + 1);
//            Log.d("hello", linearLayoutID);
//            resID = getResources().getIdentifier(linearLayoutID, "id", "com.android.ground.ground");
//            if(findViewById(resID)!= null) {
//                Log.d("hello", "ok");
//                findViewById(resID).setOnDragListener(new View.OnDragListener() {
//                    @Override
//                    public boolean onDrag(View v, DragEvent event) {
//                        switch (event.getAction()) {
//                            case DragEvent.ACTION_DRAG_STARTED:
//                                // do nothing
//                                break;
//                            case DragEvent.ACTION_DRAG_ENTERED:
////                            v.setBackgroundDrawable(enterShape);
//                                break;
//                            case DragEvent.ACTION_DRAG_EXITED:
////                            v.setBackgroundDrawable(normalShape);
//                                break;
//                            case DragEvent.ACTION_DROP:
//                                // Dropped, reassign View to ViewGroup
//                                View view = (View) event.getLocalState();
//                                String text = event.getClipData().getItemAt(0).getText().toString();
//                                Log.d("hello", " view : " + text);
////                    ViewGroup owner = (ViewGroup) view.getParent();
////                    owner.removeView(view);
//                                LinearLayout container = (LinearLayout) v;
////                            gridItemView.setTextView(((ListItemView)view).getTextView());
//                                gridItemView = new GridItemView2(Main24Activity.this);
//                                container.addView(gridItemView);
////                            view.setVisibility(View.VISIBLE);
//                                break;
//                            case DragEvent.ACTION_DRAG_ENDED:
////                            v.setBackgroundDrawable(normalShape);
//                            default:
//                                break;
//                        }
//                        return true;
//
//                    }
//                });
//            }
//        }
//        listItemView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                String text = ((ListItemView) v).getTextView().toString();
//                ClipData.Item item = new ClipData.Item(text);
//                ClipData data = new ClipData(text, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
//                v.startDrag(data, new View.DragShadowBuilder(v), null, 0);
//                return true;
//            }
//        });

        //====================================================리스너

//        mAdapter= new ArrayAdapter<String>(this, android.R.layout.activity_list_item);
//        gridLayout.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                switch (event.getAction()) {
//                    case DragEvent.ACTION_DRAG_STARTED :
//                        return true;
//                    case DragEvent.ACTION_DRAG_ENTERED :
//                        gridLayout.setBackgroundColor(Color.GREEN);
//                        return true;
//                    case DragEvent.ACTION_DRAG_EXITED :
//                        gridLayout.setBackgroundColor(Color.TRANSPARENT);
//                        return true;
//                    case DragEvent.ACTION_DROP :
//
//                        View view = (View)event.getLocalState();
//                        GridLayout container = (GridLayout)v;
//                        container.addView(view, 1);
//
//                        view.setVisibility(View.VISIBLE);
////                        ViewGroup owner = (ViewGroup) view.getParent();
////                        owner.removeView(view);
//
//
//                        float x = event.getX();
//                        float y = event.getY();
//                        String text = event.getClipData().getItemAt(0).getText().toString();
//
////                        int position = gridLayout.pointToPosition((int)x, (int)y);
//
////                        if (position != ListView.INVALID_POSITION) {
////                            mAdapter.insert(text, position);
////                        } else {
////                            mAdapter.add(text);
////                        }
//                        return true;
//                    case DragEvent.ACTION_DRAG_ENDED :
//                        boolean isDrop = event.getResult();
//                        if (isDrop) {
//                            Toast.makeText(Main2Activity.this, "My", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(Main2Activity.this, "No", Toast.LENGTH_SHORT).show();
//                        }
//                        return true;
//                }
//                return false;
//            }
//        });
//        View.OnTouchListener mClickListener = new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                String text = ((ListItemView)v).getTextView().toString();
//                ClipData.Item item =  new ClipData.Item(text);
//                ClipData data = new ClipData(text, new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN},item);
//                v.startDrag(data, new View.DragShadowBuilder(v), null, 0);
//                return true;
//            }
//        };

    }//create


}
