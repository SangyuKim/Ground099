package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.fc.fcmain.ClubMatchList.ClubMatchListResult;
import com.android.ground.ground.view.OnDialogClickListener;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class FCMatchHistoryItemView extends FrameLayout {
    TextView homeClubName, awayClubName, homeScore, awayScore
            , startTime,matchDate, matchDay;
    ClubMatchListResult mItem;

    public FCMatchHistoryItemView(Context context) {
        super(context);
        init();
    }

    public void init(){
        inflate(getContext(), R.layout.custom_controller_fc_fcmain_matchhistory_list, this);
        homeClubName = (TextView)findViewById(R.id.homeClubName);
        awayClubName = (TextView)findViewById(R.id.awayClubName);
        homeScore = (TextView)findViewById(R.id.homeScore);
        awayScore = (TextView)findViewById(R.id.awayScore);
        startTime = (TextView)findViewById(R.id.startTime);
        matchDate = (TextView)findViewById(R.id.matchDate);
        matchDay = (TextView)findViewById(R.id.matchDay);

        Button btn = (Button)findViewById(R.id.button27);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialogListener != null){
                    mDialogListener.onDialogClick(FCMatchHistoryItemView.this);
                }
            }
        });
    }
    public void setFCMatchHistoryListItem(ClubMatchListResult item) {
        mItem = item;
        homeClubName.setText(item.homeClubName);
        awayClubName.setText(item.awayClubName);
        homeScore.setText(Integer.toString(item.homeScore));
        awayScore.setText(Integer.toString(item.awayScore));
        startTime.setText(item.startTime);
        matchDate.setText(item.matchDate);
        matchDay.setText(Integer.toString(item.matchDay));

    }
    OnDialogClickListener mDialogListener;
    public void setOnDialogListener(OnDialogClickListener listener) {
        mDialogListener = listener;
    }

}
