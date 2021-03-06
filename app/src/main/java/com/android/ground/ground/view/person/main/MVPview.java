package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.Utils;
import com.android.ground.ground.model.person.main.matchinfo.MVP.MVP;
import com.android.ground.ground.model.person.main.matchinfo.MVP.MVPResult;
import com.android.ground.ground.model.person.main.matchinfo.MVP.ScrResult;
import com.android.ground.ground.model.person.main.matchinfo.MVP.WinResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class MVPview extends FrameLayout {

    ImageView memImageMVP, memImageScr, clubImage, clubImageMVP, clubImageScr, scrPlayerPosition, mvpPlayerPosition ;
    TextView month, memNameCountMVP, memNameCountScr, clubNmaeCount;
    MVP mMVP;
    public  MVPResult itemMVP;
    public ScrResult itemScr;
    public WinResult itemWin;

    DisplayImageOptions options;




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
        clubImageMVP =(ImageView)findViewById(R.id.clubImageMVP);
        clubImageScr =(ImageView)findViewById(R.id.clubImageScr);
        month =(TextView)findViewById(R.id.month);
        memNameCountMVP =(TextView)findViewById(R.id.memNameCountMVP);
        memNameCountScr =(TextView)findViewById(R.id.memNameCountScr);
        clubNmaeCount = (TextView)findViewById(R.id.clubNmaeCount);


        scrPlayerPosition =(ImageView)findViewById(R.id.scrPlayerPosition);
        mvpPlayerPosition =(ImageView)findViewById(R.id.mvpPlayerPosition);



        memImageMVP = (ImageView)findViewById(R.id.memImage);
        memImageMVP.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRequestListener != null) {
                    mRequestListener.onHeaderImageClick(MVPview.this, "MVP");
                }
            }
        });
        memImageScr = (ImageView)findViewById(R.id.memImageScr);
        memImageScr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRequestListener != null) {
                    mRequestListener.onHeaderImageClick(MVPview.this, "SCR");
                }
            }
        });
        clubImage = (ImageView)findViewById(R.id.clubImage);
        clubImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRequestListener != null) {
                    mRequestListener.onHeaderImageClick(MVPview.this, "CLUB");
                }
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

    }
    public void setMVP(MVP items){
        mMVP = items;

        itemMVP = items.resultMVP;
        itemScr = items.resultScr;
        itemWin = items.resultWin;

        month.setText(items.month +"월의 MVP" );
        memNameCountMVP.setText(itemMVP.memName + " / " + itemMVP.count +" 회");
        memNameCountScr.setText(itemScr.memName + " / " + itemScr.count + " 골");
        clubNmaeCount.setText(itemWin.clubName + " / " + itemMVP.count + " 승");

        ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + itemMVP.clubImage), clubImageMVP, options);
        ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + itemMVP.memImage), memImageMVP, options);
        ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + itemScr.clubImage), clubImageScr, options);
        ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + itemScr.memImage), memImageScr, options);
        ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + itemWin.clubImage), clubImage, options);


//int a=  Utils.POSITION_AM;
//
//        switch (){
//            case Utils.POSITION_AM :{
//
//                break;
//            }
//        }

        if(itemScr.position>0 &&itemScr.position < 15)
             scrPlayerPosition.setImageResource(Utils.POSITIONS[itemScr.position-1]);
        if(itemMVP.position>0 &&itemMVP.position< 15)
            mvpPlayerPosition.setImageResource(Utils.POSITIONS[itemMVP.position-1]);

    }

    public interface OnHeaderImageClickListener {
        public void onHeaderImageClick(MVPview view, String tag);
    }

    OnHeaderImageClickListener mRequestListener;
    public void setOnHeaderImageListener(OnHeaderImageClickListener listener) {
        mRequestListener = listener;
    }


    public ImageView getMemImageMVP() {
        return memImageMVP;
    }

    public ImageView getMemImageScr() {
        return memImageScr;
    }

    public ImageView getClubImage() {
        return clubImage;
    }

    public ImageView getClubImageMVP() {
        return clubImageMVP;
    }

    public ImageView getClubImageScr() {
        return clubImageScr;
    }

    public TextView getMonth() {
        return month;
    }

    public TextView getMemNameCountMVP() {
        return memNameCountMVP;
    }

    public TextView getMemNameCountScr() {
        return memNameCountScr;
    }

    public TextView getClubNmaeCount() {
        return clubNmaeCount;
    }

    public MVP getmMVP() {
        return mMVP;
    }

    public MVPResult getItemMVP() {
        return itemMVP;
    }

    public ScrResult getItemScr() {
        return itemScr;
    }

    public WinResult getItemWin() {
        return itemWin;
    }

    public DisplayImageOptions getOptions() {
        return options;
    }
}
