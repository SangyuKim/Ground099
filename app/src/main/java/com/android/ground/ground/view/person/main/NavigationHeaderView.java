package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.ground.ground.R;
import com.android.ground.ground.view.OnCustomImageClickListener;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class NavigationHeaderView extends FrameLayout {
    ImageView imageView;
    public NavigationHeaderView(Context context) {
        super(context);
        init();
    }

    public NavigationHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void init(){
        inflate(getContext(), R.layout.nav_header_main, this);
        imageView = (ImageView)findViewById(R.id.imageView_nav_header);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null)
                      mListener.OnCustomImageClick(NavigationHeaderView.this);
            }
        });


    }

    OnCustomImageClickListener mListener;
    public void setOnCustomImageClickListener(OnCustomImageClickListener listener){
            mListener = listener;

    }
}
