package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.custom.CustomDragDropListView;
import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualResResult;

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
        inflate(getContext(), R.layout.view_ready_match_result_list_item, this);
        memName = (TextView)findViewById(R.id.memName);
        position = (ImageView)findViewById(R.id.position);

    }
    public void setReadyMatchResultListItem(LineupVirtualResResult item){
        mItem = item;
        memName.setText(item.memName);
        // position 별로 이미지 넣기
        position.setImageResource(R.mipmap.ic_launcher);

    }



}
