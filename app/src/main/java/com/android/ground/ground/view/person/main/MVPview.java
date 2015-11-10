package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.person.profile.MyProfileActivity;
import com.android.ground.ground.model.Profile;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class MVPview extends FrameLayout {
    Profile mProfile1, mProfile2, mProfile3;
    ImageView memImageMVP, memImageScr, clubImage;
    TextView month, memNameCountMVP, memNameCountScr, clubNmaeCount;

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
        memImageMVP = (ImageView)findViewById(R.id.memImageMVP);
        mProfile1 = new MyProfileActivity();
        memImageMVP.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRequestListener != null) {
                    mRequestListener.onHeaderImageClick(MVPview.this, mProfile1);
                }
            }
        });
        memImageScr = (ImageView)findViewById(R.id.memImageScr);
        mProfile2 = new MyProfileActivity();
        memImageScr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRequestListener != null) {
                    mRequestListener.onHeaderImageClick(MVPview.this, mProfile2);
                }
            }
        });
        clubImage = (ImageView)findViewById(R.id.clubImage);
        mProfile3 = new FCActivity();
        clubImage.setOnClickListener(new OnClickListener() {
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
