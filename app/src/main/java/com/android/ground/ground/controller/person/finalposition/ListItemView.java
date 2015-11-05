package com.android.ground.ground.controller.person.finalposition;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;

/**
 * Created by 상유 on 2015-10-23.
 */

public class ListItemView extends FrameLayout{
    TextView textView;
    ImageView imageView;
    public ListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void init(){
        inflate(getContext(), R.layout.list_layout, this);
        textView = (TextView)findViewById(R.id.textView);
        imageView = (ImageView)findViewById(R.id.imageView);
    }

    public TextView getTextView() {
        return textView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
