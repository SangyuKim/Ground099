package com.android.ground.ground.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.person.main.matchinfo.MVP.MVP;
import com.android.ground.ground.model.person.main.matchinfo.MVP.MVPResult;
import com.android.ground.ground.model.person.main.matchinfo.MVP.ScrResult;
import com.android.ground.ground.model.person.main.matchinfo.MVP.WinResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class CustomToolbar extends FrameLayout {

    TextView textViewTitle;



    public CustomToolbar(Context context) {
        super(context);
        init();
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.custom_toolbar_layout, this);
        textViewTitle = (TextView)findViewById(R.id.textViewTitle);


    }
    public void setTitle(String title){
        textViewTitle.setText(title);

    }

    public TextView getTextViewTitle() {
        return textViewTitle;
    }
}
