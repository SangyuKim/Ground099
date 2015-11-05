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
public class GridItemView2 extends FrameLayout {
    ImageView imageView;

    public GridItemView2(Context context) {
        super(context);
        init();
    }

    TextView textView;
    public GridItemView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void init(){
       inflate(getContext(), R.layout.gird_item2_layout, this);
        imageView = (ImageView)findViewById(R.id.imageView2);
        textView= (TextView)findViewById(R.id.textView2);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
