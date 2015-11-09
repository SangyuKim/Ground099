package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.android.ground.ground.R;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class NavigationHeaderView extends FrameLayout {
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
    }
}
