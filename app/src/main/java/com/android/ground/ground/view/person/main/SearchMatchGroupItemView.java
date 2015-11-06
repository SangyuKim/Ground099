package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.person.main.CheckMatchListGroupItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class SearchMatchGroupItemView extends RelativeLayout {
    public SearchMatchGroupItemView(Context context) {
        super(context);
        init();
    }

    public SearchMatchGroupItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView textView;
    RelativeLayout mRelativeLayout;

    private void init() {
        inflate(getContext(), R.layout.custom_controller_person_main_checkamatch_group, this);
        textView = (TextView)findViewById(R.id.textView83);
        mRelativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_group);
    }

    public void setGroupItem(CheckMatchListGroupItem item){
        textView.setText(item.text);
        mRelativeLayout.setBackgroundColor(item.color);
    }

}