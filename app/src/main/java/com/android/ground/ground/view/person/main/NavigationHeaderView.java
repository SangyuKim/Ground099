package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.person.profile.MyPageResult;
import com.android.ground.ground.view.OnCustomImageClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class NavigationHeaderView extends FrameLayout {
    ImageView memImage, clubImage, managerYN;
    TextView memName, memIntro;

    DisplayImageOptions options;

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
        memImage = (ImageView)findViewById(R.id.memImage);
        clubImage= (ImageView)findViewById(R.id.clubImage);
        managerYN = (ImageView)findViewById(R.id.managerYN);
        memName =(TextView)findViewById(R.id.memName);
        memIntro= (TextView)findViewById(R.id.memIntro);


        memImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null)
                      mListener.OnCustomImageClick(NavigationHeaderView.this);
            }
        });

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(50))
                .build();

        MyPageResult item = PropertyManager.getInstance().getMyPageResult();

        ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + item.memImage), memImage, options);
        ImageLoader.getInstance().displayImage((PropertyManager.ImageUrl + item.clubImage), clubImage, options);

        memName.setText(item.memName);
        memIntro.setText(item.memIntro);

        if(item.managerYN ==0 ){
            managerYN.setVisibility(View.INVISIBLE);
        }else{
            managerYN.setVisibility(View.VISIBLE);
        }


    }

    OnCustomImageClickListener mListener;
    public void setOnCustomImageClickListener(OnCustomImageClickListener listener){
            mListener = listener;

    }
}
