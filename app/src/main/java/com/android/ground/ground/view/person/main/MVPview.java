package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.person.profile.MyProfileActivity;
import com.android.ground.ground.model.Profile;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class MVPview extends FrameLayout {
    Profile mProfile1, mProfile2, mProfile3;
    ImageView imageView;
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
        imageView = (ImageView)findViewById(R.id.imageView2);
        mProfile1 = new MyProfileActivity();
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRequestListener != null) {
                    mRequestListener.onHeaderImageClick(MVPview.this, mProfile1);
                }
            }
        });
        imageView = (ImageView)findViewById(R.id.imageView3);
        mProfile2 = new MyProfileActivity();
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRequestListener != null) {
                    mRequestListener.onHeaderImageClick(MVPview.this, mProfile2);
                }
            }
        });
        imageView = (ImageView)findViewById(R.id.imageView4);
        mProfile3 = new FCActivity();
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRequestListener != null) {
                    mRequestListener.onHeaderImageClick(MVPview.this, mProfile3);
                }
            }
        });

    }

    public interface OnHeaderImageClickListener {
        public void onHeaderImageClick(MVPview view, Profile data);
    }

    OnHeaderImageClickListener mRequestListener;
    public void setOnHeaderImageListener(OnHeaderImageClickListener listener) {
        mRequestListener = listener;
    }
}
