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
import com.android.ground.ground.model.person.main.searchClub.SearchClubResult;
import com.android.ground.ground.view.OnSpecificDialogClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SearchFCItemView extends RelativeLayout {
    public final static String ImageUrl ="https://s3-ap-northeast-1.amazonaws.com/";
    public SearchFCItemView(Context context) {
        super(context);
        init();
    }

    public SearchFCItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView clubImage, memYN, matchYN;
    TextView clubName, clubField, clubSkill, clubManner,clubLocationName;
    SearchClubResult mItem;
    CheckBox clubMainDay_Mon,clubMainDay_Tue,clubMainDay_Wed,
            clubMainDay_Thu,clubMainDay_Fri,clubMainDay_Sat,clubMainDay_Sun;

    DisplayImageOptions options;

    private void init() {
        inflate(getContext(), R.layout.view_search_fc_test_item, this);
        clubImage = (ImageView)findViewById(R.id.memImageMVP);
        clubName = (TextView)findViewById(R.id.memNameCountScr);
        clubLocationName = (TextView)findViewById(R.id.clubLocationName);
        clubField = (TextView)findViewById(R.id.clubField);
        clubSkill = (TextView)findViewById(R.id.clubSkill);
        clubManner= (TextView)findViewById(R.id.clubManner);
        clubMainDay_Mon =(CheckBox)findViewById(R.id.clubMainDay_Mon);
        clubMainDay_Tue =(CheckBox)findViewById(R.id.clubMainDay_Tue);
        clubMainDay_Wed =(CheckBox)findViewById(R.id.clubMainDay_Wed);
        clubMainDay_Thu =(CheckBox)findViewById(R.id.clubMainDay_Thu);
        clubMainDay_Fri =(CheckBox)findViewById(R.id.clubMainDay_Fri);
        clubMainDay_Sat =(CheckBox)findViewById(R.id.clubMainDay_Sat);
        clubMainDay_Sun =(CheckBox)findViewById(R.id.clubMainDay_Sun);
        memYN = (ImageView)findViewById(R.id.memYN);
        matchYN = (ImageView)findViewById(R.id.matchYN);
        Button btn;

        btn = (Button)findViewById(R.id.button21);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                   if (mRequestListener != null) {
                    mRequestListener.onDialogClick(SearchFCItemView.this, "입단신청");
                }
            }
        });
        btn = (Button)findViewById(R.id.button34);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRequestListener != null) {
                    mRequestListener.onDialogClick(SearchFCItemView.this, "매치신청");
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

    public void setSearchClubResult(SearchClubResult item) {
        mItem = item;
        clubName.setText(item.clubName);
        clubLocationName.setText(item.clubLocationName);
        clubField.setText(item.clubField);
        clubSkill.setText(Double.toString(item.clubSkill));
        clubManner.setText(Double.toString(item.clubManner));
        if(item.clubMainDay_Mon ==0)
            clubMainDay_Mon.setChecked(false);
        else
            clubMainDay_Mon.setChecked(true);
        if(item.clubMainDay_Tue ==0)
            clubMainDay_Tue.setChecked(false);
        else
            clubMainDay_Tue.setChecked(true);
        if(item.clubMainDay_Wed ==0)
            clubMainDay_Wed.setChecked(false);
        else
            clubMainDay_Wed.setChecked(true);
        if(item.clubMainDay_Thu ==0)
            clubMainDay_Thu.setChecked(false);
        else
            clubMainDay_Thu.setChecked(true);
        if(item.clubMainDay_Fri ==0)
            clubMainDay_Fri.setChecked(false);
        else
            clubMainDay_Fri.setChecked(true);
        if(item.clubMainDay_Mon ==0)
            clubMainDay_Sat.setChecked(false);
        else
            clubMainDay_Sat.setChecked(true);
        if(item.clubMainDay_Sun ==0)
            clubMainDay_Sun.setChecked(false);
        else
            clubMainDay_Sun.setChecked(true);
        ImageLoader.getInstance().displayImage((ImageUrl+item.clubImage), clubImage, options);
        if(item.memYN ==0)
            memYN.setVisibility(View.INVISIBLE);
        else{
            memYN.setVisibility(View.VISIBLE);
        }
        if(item.matchYN ==0)
            matchYN.setVisibility(View.INVISIBLE);
        else{
            matchYN.setVisibility(View.VISIBLE);
        }
    }


    OnSpecificDialogClickListener mRequestListener;
    public void setOnSpecificDialogListener(OnSpecificDialogClickListener listener) {
        mRequestListener = listener;
    }

    public static String getImageUrl() {
        return ImageUrl;
    }

    public ImageView getClubImage() {
        return clubImage;
    }

    public ImageView getMemYN() {
        return memYN;
    }

    public ImageView getMatchYN() {
        return matchYN;
    }

    public TextView getClubName() {
        return clubName;
    }

    public TextView getClubField() {
        return clubField;
    }

    public TextView getClubSkill() {
        return clubSkill;
    }

    public TextView getClubManner() {
        return clubManner;
    }

    public TextView getClubLocationName() {
        return clubLocationName;
    }

    public SearchClubResult getmItem() {
        return mItem;
    }

    public CheckBox getClubMainDay_Mon() {
        return clubMainDay_Mon;
    }

    public CheckBox getClubMainDay_Tue() {
        return clubMainDay_Tue;
    }

    public CheckBox getClubMainDay_Wed() {
        return clubMainDay_Wed;
    }

    public CheckBox getClubMainDay_Thu() {
        return clubMainDay_Thu;
    }

    public CheckBox getClubMainDay_Fri() {
        return clubMainDay_Fri;
    }

    public CheckBox getClubMainDay_Sat() {
        return clubMainDay_Sat;
    }

    public CheckBox getClubMainDay_Sun() {
        return clubMainDay_Sun;
    }


    public DisplayImageOptions getOptions() {
        return options;
    }
}