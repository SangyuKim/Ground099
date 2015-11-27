package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.graphics.Color;
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
//        imageViewBar = (ImageView)findViewById(R.id.imageView5);
    }

    public void setGroupItem(CheckMatchListGroupItem item){
        textView.setText(item.text);
        mRelativeLayout.setBackgroundColor(item.color);

        if(item.text =="예정된 매치"){
//            imageViewBar.setVisibility(View.VISIBLE);
//             imageViewBar.setBackgroundColor(getResources().getColor(R.color.red));
            textView.setPadding(0, getContext().getResources().getDimensionPixelSize(R.dimen.your_dimension_name), 0, getContext().getResources().getDimensionPixelSize(R.dimen.your_dimension_name));
            textView.setTextColor(Color.WHITE);
        }else if(item.text.equals("기록 대기중 매치")){
//            imageViewBar.setVisibility(View.VISIBLE);
//             imageViewBar.setBackgroundColor(getResources().getColor(R.color.gray));
            textView.setPadding(0, getContext().getResources().getDimensionPixelSize(R.dimen.your_dimension_name), 0,getContext().getResources().getDimensionPixelSize(R.dimen.your_dimension_name));
            textView.setTextColor(Color.WHITE);
        }else  if(item.text.equals("종료된 매치")){
//            imageViewBar.setVisibility(View.VISIBLE);
//            imageViewBar.setBackgroundColor(getResources().getColor(R.color.blue));
            textView.setPadding(0, getContext().getResources().getDimensionPixelSize(R.dimen.your_dimension_name), 0, getContext().getResources().getDimensionPixelSize(R.dimen.your_dimension_name));
            textView.setTextColor(Color.WHITE);
        }else if(item.text.equals("예정된 매치 더보기")){
//            imageViewBar.setVisibility(View.GONE);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 0, 0, getContext().getResources().getDimensionPixelSize(R.dimen.your_dimension_name));
            textView.setTextColor(getResources().getColor(R.color.spinner));
        }else if(item.text.equals("기록 대기중 매치 더보기")){
//            imageViewBar.setVisibility(View.GONE);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 0, 0, getContext().getResources().getDimensionPixelSize(R.dimen.your_dimension_name));
            textView.setTextColor(getResources().getColor(R.color.spinner));
        }else if(item.text.equals("종료된 매치 더보기")){
//            imageViewBar.setVisibility(View.GONE);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, 0, 0, getContext().getResources().getDimensionPixelSize(R.dimen.your_dimension_name));
            textView.setTextColor(getResources().getColor(R.color.spinner));

        }
    }

    public TextView getTextView() {
        return textView;
    }
}
