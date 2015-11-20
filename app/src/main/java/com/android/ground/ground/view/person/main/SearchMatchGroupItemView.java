package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.person.main.matchinfo.CheckMatchListGroupItem;

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
    ImageView imageViewBar;

    private void init() {
        inflate(getContext(), R.layout.custom_controller_person_main_checkamatch_group, this);
        textView = (TextView)findViewById(R.id.textView83);
        mRelativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_group);
        imageViewBar = (ImageView)findViewById(R.id.imageView5);
    }

    public void setGroupItem(CheckMatchListGroupItem item){
        textView.setText(item.text);
        mRelativeLayout.setBackgroundColor(item.color);

        if(item.text =="futureMat"){
            imageViewBar.setVisibility(View.VISIBLE);
            textView.setGravity(Gravity.LEFT);
             imageViewBar.setBackgroundColor(getResources().getColor(R.color.red));
        }else if(item.text.equals("ingMat")){
            imageViewBar.setVisibility(View.VISIBLE);
            textView.setGravity(Gravity.LEFT);
             imageViewBar.setBackgroundColor(getResources().getColor(R.color.gray));
        }else  if(item.text.equals("endMat")){
            imageViewBar.setVisibility(View.VISIBLE);
            textView.setGravity(Gravity.LEFT);
            imageViewBar.setBackgroundColor(getResources().getColor(R.color.blue));
        }else if(item.text.equals("더보기1")){
            imageViewBar.setVisibility(View.GONE);
            textView.setGravity(Gravity.CENTER);
        }else if(item.text.equals("더보기2")){
            imageViewBar.setVisibility(View.GONE);
            textView.setGravity(Gravity.CENTER);
        }else if(item.text.equals("더보기3")){
            imageViewBar.setVisibility(View.GONE);
            textView.setGravity(Gravity.CENTER);

        }
    }

    public TextView getTextView() {
        return textView;
    }
}
