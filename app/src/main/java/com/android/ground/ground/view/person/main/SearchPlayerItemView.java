package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.person.main.searchMem.SearchMemResult;
import com.android.ground.ground.view.OnSpecificDialogClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SearchPlayerItemView extends RelativeLayout {
    public final static String ImageUrl ="https://s3-ap-northeast-1.amazonaws.com/";
    public SearchPlayerItemView(Context context) {
        super(context);
        init();
    }

    public SearchPlayerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView memImage, clubYN, managerYN, clubImage;
    TextView memName, age, memLocationName, memIntro;
    SearchMemResult mItem;
    CheckBox memMainDay_Mon,memMainDay_Tue,memMainDay_Wed,
            memMainDay_Thu,memMainDay_Fri,memMainDay_Sat,memMainDay_Sun;
    LinearLayout mLinearLayout;

    ImageView btnRequest;
    Button btn;
    DisplayImageOptions options;

    private void init() {
        inflate(getContext(), R.layout.view_search_player_test_item, this);
        memImage = (ImageView)findViewById(R.id.memImage);
        clubImage = (ImageView)findViewById(R.id.clubImage);
        memName = (TextView)findViewById(R.id.memNameCountScr);
        age = (TextView)findViewById(R.id.age);
        memLocationName = (TextView)findViewById(R.id.memLocationName);
        memIntro= (TextView)findViewById(R.id.memIntro);
        memMainDay_Mon =(CheckBox)findViewById(R.id.memMainDay_Mon);
        memMainDay_Tue =(CheckBox)findViewById(R.id.memMainDay_Tue);
        memMainDay_Wed =(CheckBox)findViewById(R.id.memMainDay_Wed);
        memMainDay_Thu =(CheckBox)findViewById(R.id.memMainDay_Thu);
        memMainDay_Fri =(CheckBox)findViewById(R.id.memMainDay_Fri);
        memMainDay_Sat =(CheckBox)findViewById(R.id.memMainDay_Sat);
        memMainDay_Sun =(CheckBox)findViewById(R.id.memMainDay_Sun);

        clubYN = (ImageView)findViewById(R.id.clubYN);
        managerYN = (ImageView)findViewById(R.id.managerYN);



        btnRequest = (ImageView)findViewById(R.id.button21);
        btnRequest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                   if (mRequestListener != null) {
                    mRequestListener.onDialogClick(SearchPlayerItemView.this, "영입하기");
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

    public void setSearchMemResult(SearchMemResult item) {
        mItem = item;
        memName.setText(item.memName);
        age.setText("("+Integer.toString(item.age)+")");
        memLocationName.setText(item.memLocationName);
        memIntro.setText(item.memIntro);
        if(item.memMainDay_Mon ==0){
            memMainDay_Mon.setChecked(false);
        }else{
            memMainDay_Mon.setChecked(true);
        }
        if(item.memMainDay_Tue ==0){
            memMainDay_Tue.setChecked(false);
        }else{
            memMainDay_Tue.setChecked(true);
        }
        if(item.memMainDay_Wed ==0){
            memMainDay_Wed.setChecked(false);
        }else{
            memMainDay_Wed.setChecked(true);
        }
        if(item.memMainDay_Thu ==0){
            memMainDay_Thu.setChecked(false);
        }else{
            memMainDay_Thu.setChecked(true);
        }
        if(item.memMainDay_Fri ==0){
            memMainDay_Fri.setChecked(false);
        }else{
            memMainDay_Fri.setChecked(true);
        }
        if(item.memMainDay_Sat ==0){
            memMainDay_Sat.setChecked(false);
        }else{
            memMainDay_Sat.setChecked(true);
        }
        if(item.memMainDay_Sun ==0){
            memMainDay_Sun.setChecked(false);
        }else{
            memMainDay_Sun.setChecked(true);
        }
        if(item.clubYN==0){
            clubYN.setImageResource(R.drawable.match_201);
        }else{
            clubYN.setImageResource(R.drawable.match_202);
        }
        if(item.managerYN==0){
            managerYN.setImageResource(R.drawable.icon201);
        }else{
            managerYN.setImageResource(R.drawable.captain);
        }


        ImageLoader.getInstance().displayImage(ImageUrl+item.memImage, memImage, options);
        ImageLoader.getInstance().displayImage(ImageUrl+item.clubImage, clubImage, options);
    }

    OnSpecificDialogClickListener mRequestListener;
    public void setOnSpecificDialogListener(OnSpecificDialogClickListener listener) {
        mRequestListener = listener;
    }

    public static String getImageUrl() {
        return ImageUrl;
    }

    public ImageView getMemImage() {
        return memImage;
    }

    public ImageView getClubYN() {
        return clubYN;
    }

    public ImageView getManagerYN() {
        return managerYN;
    }

    public TextView getMemName() {
        return memName;
    }

    public TextView getAge() {
        return age;
    }

    public TextView getMemLocationName() {
        return memLocationName;
    }

    public TextView getMemIntro() {
        return memIntro;
    }

    public SearchMemResult getmItem() {
        return mItem;
    }

    public CheckBox getMemMainDay_Mon() {
        return memMainDay_Mon;
    }

    public CheckBox getMemMainDay_Tue() {
        return memMainDay_Tue;
    }

    public CheckBox getMemMainDay_Wed() {
        return memMainDay_Wed;
    }

    public CheckBox getMemMainDay_Thu() {
        return memMainDay_Thu;
    }

    public CheckBox getMemMainDay_Fri() {
        return memMainDay_Fri;
    }

    public CheckBox getMemMainDay_Sat() {
        return memMainDay_Sat;
    }

    public CheckBox getMemMainDay_Sun() {
        return memMainDay_Sun;
    }

    public LinearLayout getmLinearLayout() {
        return mLinearLayout;
    }

    public DisplayImageOptions getOptions() {
        return options;
    }
}
