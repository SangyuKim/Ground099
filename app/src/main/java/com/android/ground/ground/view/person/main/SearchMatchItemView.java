package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.model.person.main.matchinfo.MatchInfoResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchMatchItemView extends RelativeLayout implements Checkable {
    public final static String ImageUrl = "https://s3-ap-northeast-1.amazonaws.com/";




    public SearchMatchItemView(Context context) {
        super(context);
        init();
    }

    public SearchMatchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    TextView matchDate, startTime, homeClubName, awayClubName, matchLocation, mvpName
            , matchDay, homeScore, awayScore, matchDD;
    MatchInfoResult mItem;
    RelativeLayout mRelatveLayout;
    Button btn;
    ImageView imageViewBar;


    DisplayImageOptions options;

    private void init() {
        inflate(getContext(), R.layout.view_search_match_test_item, this);

        matchDate = (TextView) findViewById(R.id.matchDate);
        homeClubName = (TextView) findViewById(R.id.homeClubName);
        awayClubName = (TextView) findViewById(R.id.awayClubName);
        matchDay = (TextView) findViewById(R.id.matchDay);
        homeScore = (TextView) findViewById(R.id.homeScore);
        awayScore = (TextView) findViewById(R.id.awayScore);
        matchDD= (TextView)findViewById(R.id.matchDD);


        imageViewBar= (ImageView)findViewById(R.id.imageView6);

//        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_extra);
        mRelatveLayout = (RelativeLayout)findViewById(R.id.relativeLayout_extra);
        btn = (Button) findViewById(R.id.button36);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.setOnButtonClick(SearchMatchItemView.this);
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

    public void setMatchInfoResult(MatchInfoResult item) {
        mItem = item;
        Date to = new Date();
        String dd ="" ;
        String month ="";
        int flag = 0;
        if(item.matchDate != null){
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy/MM/dd");
            try {
                to = transFormat.parse(item.matchDate);
                flag++;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        SimpleDateFormat transFormat1 = new SimpleDateFormat("MM");
        SimpleDateFormat transFormat2 = new SimpleDateFormat("dd");
        if(flag !=0 ) {
            dd = transFormat2.format(to);
           month = transFormat1.format(to);

        }
        matchDate.setText(month + "/");
//        Toast.makeText(getContext(), dd, Toast.LENGTH_SHORT).show();
        matchDD.setText(dd);
        homeClubName.setText(item.homeClubName);
        awayClubName.setText(item.awayClubName);
        String day = "";
        if (item.matchDay == 1) {
            day = "  Sat.";
        } else if (item.matchDay == 2) {
            day = "  Mon.";
        } else if (item.matchDay == 3) {
            day = "  Tue.";
        } else if (item.matchDay == 4) {
            day = "  Wed.";
        } else if (item.matchDay == 5) {
            day = "  Thu.";
        } else if (item.matchDay == 6) {
            day = "  Fri.";
        } else if (item.matchDay == 7) {
            day = "  Sat.";
        }
        matchDay.setText(day);
        homeScore.setText(Integer.toString(item.homeScore));
        awayScore.setText(Integer.toString(item.awayScore));

        if(item.insertResultYN == 1){
            imageViewBar.setBackgroundColor(getResources().getColor(R.color.blue));
        }else if(item.insertResultYN ==4){
            imageViewBar.setBackgroundColor(getResources().getColor(R.color.red));
        }else{
            imageViewBar.setBackgroundColor(getResources().getColor(R.color.gray));
        }


    }

    public void setVisible() {
        mRelatveLayout.setVisibility(View.VISIBLE);
    }

    public void setInvisible() {
        mRelatveLayout.setVisibility(View.GONE);
    }

    public int getVisibilityLayout() {
        return mRelatveLayout.getVisibility();
    }

    @Override
    public void setChecked(boolean checked) {
         if (checked != isChecked) {
            isChecked = checked;
            drawCheck();

        }

    }

    boolean isChecked = false;

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    private void drawCheck() {
        if (isChecked) {
//            checkView.setImageResource(android.R.drawable.checkbox_on_background);
//            setBackgroundColor(Color.RED);
//           checkedImage.setVisibility(View.VISIBLE);
            setVisible();
        } else {
//            checkView.setImageResource(android.R.drawable.checkbox_off_background);
//            setBackgroundColor(Color.TRANSPARENT);
//            checkedImage.setVisibility(View.GONE);
            setInvisible();
        }

    }

    public static String getImageUrl() {
        return ImageUrl;
    }

    public TextView getMatchDate() {
        return matchDate;
    }

    public TextView getStartTime() {
        return startTime;
    }

    public TextView getHomeClubName() {
        return homeClubName;
    }

    public TextView getAwayClubName() {
        return awayClubName;
    }

    public TextView getMatchLocation() {
        return matchLocation;
    }

    public TextView getMvpName() {
        return mvpName;
    }

    public TextView getMatchDay() {
        return matchDay;
    }

    public TextView getHomeScore() {
        return homeScore;
    }

    public TextView getAwayScore() {
        return awayScore;
    }

    public MatchInfoResult getmItem() {
        return mItem;
    }

//    public LinearLayout getmLinearLayout() {
//        return mLinearLayout;
//    }

    public interface OnExtraButtonClickListener {
        void setOnButtonClick(View view);
    }

    OnExtraButtonClickListener mListener;

    public void setOnExtraButtonClickListner(OnExtraButtonClickListener listener) {
        mListener = listener;
    }
}
