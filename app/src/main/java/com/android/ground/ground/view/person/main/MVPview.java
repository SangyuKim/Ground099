package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.android.ground.ground.R;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class MVPview extends FrameLayout {
    public MVPview(Context context) {
        super(context);
        init();
    }

    public MVPview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public void init(){
        inflate(getContext(), R.layout.custom_controller_person_main_fragment_maincheckmatch_mvp, this);
    }
}
