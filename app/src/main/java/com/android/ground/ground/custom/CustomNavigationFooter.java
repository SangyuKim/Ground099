package com.android.ground.ground.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.view.OnCustomImageClickListener;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class CustomNavigationFooter extends FrameLayout {

    ImageView imageView;

    public CustomNavigationFooter(Context context) {
        super(context);
        init();
    }

    public CustomNavigationFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.custom_nav_item_footer, this);
        imageView = (ImageView)findViewById(R.id.imageView11);
        imageView.setOnClickListener(new OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     if (mListener != null) {
                         mListener.onCustomImageClick(CustomNavigationFooter.this);
                     }
                 }
             }
        );

    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    OnCustomImageClickListener mListener;
    public void setOnCustomImageClickListener(OnCustomImageClickListener listener){
        mListener = listener;
    }
}
