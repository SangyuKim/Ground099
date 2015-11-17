package com.android.ground.ground.view.person.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.person.main.matchinfo.MatchInfoResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

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

    TextView matchDate, startTime, homeClubName, awayClubName, matchLocation, mvpName, matchDay, homeScore, awayScore;
    MatchInfoResult mItem;
    LinearLayout mLinearLayout;
    Button btn;


    DisplayImageOptions options;

    private void init() {
        inflate(getContext(), R.layout.view_search_match_test_item, this);

        matchDate = (TextView) findViewById(R.id.matchDate);
        homeClubName = (TextView) findViewById(R.id.homeClubName);
        awayClubName = (TextView) findViewById(R.id.awayClubName);
        matchDay = (TextView) findViewById(R.id.matchDay);
        homeScore = (TextView) findViewById(R.id.homeScore);
        awayScore = (TextView) findViewById(R.id.awayScore);

        mLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_extra);
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

        matchDate.setText(item.matchDate);
        homeClubName.setText(item.homeClubName);
        awayClubName.setText(item.awayClubName);
        String day = "";
        if (item.matchDay == 1) {
            day = "토요일";
        } else if (item.matchDay == 2) {
            day = "월요일";
        } else if (item.matchDay == 3) {
            day = "화요일";
        } else if (item.matchDay == 4) {
            day = "수요일";
        } else if (item.matchDay == 5) {
            day = "목요일";
        } else if (item.matchDay == 6) {
            day = "금요일";
        } else if (item.matchDay == 7) {
            day = "토요일";
        }
        matchDay.setText(day);
        homeScore.setText(Integer.toString(item.homeScore));
        awayScore.setText(Integer.toString(item.awayScore));


    }

    public void setVisible() {
        mLinearLayout.setVisibility(View.VISIBLE);
    }

    public void setInvisible() {
        mLinearLayout.setVisibility(View.GONE);
    }

    public int getVisibilityLayout() {
        return mLinearLayout.getVisibility();
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

    public LinearLayout getmLinearLayout() {
        return mLinearLayout;
    }

    public interface OnExtraButtonClickListener {
        void setOnButtonClick(View view);
    }

    OnExtraButtonClickListener mListener;

    public void setOnExtraButtonClickListner(OnExtraButtonClickListener listener) {
        mListener = listener;
    }
}
