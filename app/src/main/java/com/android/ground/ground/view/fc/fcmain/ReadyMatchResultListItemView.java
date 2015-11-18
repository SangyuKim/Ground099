package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.custom.CustomDragDropListView;
import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualResResult;
import com.android.ground.ground.view.OnCustomTouchListener;

/**
 * Created by Tacademy on 2015-11-17.
 */
public class ReadyMatchResultListItemView extends FrameLayout {
    public ImageView position;
    public TextView memName;

    LineupVirtualResResult mItem;

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
//        view.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(mtListener !=null){
//                    mtListener.onTouch(v);
//                }
//                return true;
//            }
//        });


    }
    public void setReadyMatchResultListItem(LineupVirtualResResult item){
        mItem = item;
        memName.setText(item.memName);
        // position 별로 이미지 넣기
        position.setImageResource(R.mipmap.ic_launcher);

    }

     OnCustomTouchListener mtListener;
    public void setOnCustomTouchListener(OnCustomTouchListener listener) {
        mtListener = listener;
    }



}
