package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.custom.CustomDragDropListView;
import com.android.ground.ground.model.Utils;
import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualResResult;
import com.android.ground.ground.view.OnCustomTouchListener;

/**
 * Created by Tacademy on 2015-11-17.
 */
public class ReadyMatchResultListItemView extends FrameLayout {
    public ImageView position , dragImage;
    public TextView memName;


    public LineupVirtualResResult mItem;

    public ReadyMatchResultListItemView(Context context) {
        super(context);
        init();
    }

    public ReadyMatchResultListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void init(){
        View view = inflate(getContext(), R.layout.view_ready_match_result_list_item, this);
        memName = (TextView)findViewById(R.id.memName);
        position = (ImageView)findViewById(R.id.position);
//        dragImage.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (mtListener != null) {
//                    mtListener.onTouch(v, mItem);
//                }
//                return true;
//            }
//        });
//        view.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (mtListener != null) {
//                    mtListener.onTouch(v, mItem);
//                }
//                return true;
//            }
//        });

    }

    public void setText(String text){
        memName.setText(text);
    }
    public void setReadyMatchResultListItem(LineupVirtualResResult item){
        mItem = item;
        memName.setText(item.memName);
        // position 별로 이미지 넣기
//        position.setImageResource(R.mipmap.ic_launcher);

        if(item.position>0 &&item.position<15)
             position.setImageResource(Utils.POSITIONS[item.position-1]);

    }

     OnCustomTouchListener mtListener;
    public void setOnCustomTouchListener(OnCustomTouchListener listener) {
        mtListener = listener;
    }



}
