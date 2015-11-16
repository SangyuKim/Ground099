package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMainResult;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class FCMatchHistoryHeaderItemView extends FrameLayout{

    TextView clubName, clubFound, clubSkill, clubManner, clubAllMatchCnt, winDrawLoseNullity, scoreSumDescore;
    ClubMainResult mItem;

    public FCMatchHistoryHeaderItemView(Context context) {
        super(context);
        init();
    }
    public void init(){
        inflate(getContext(), R.layout.custom_controller_fc_fcmain_matchhistory_header, this);
        clubName= (TextView)findViewById(R.id.clubName);
        clubFound= (TextView)findViewById(R.id.clubFound);
        clubSkill= (TextView)findViewById(R.id.clubSkill);
        clubManner= (TextView)findViewById(R.id.clubManner);
        clubAllMatchCnt= (TextView)findViewById(R.id.clubAllMatchCnt);
        winDrawLoseNullity= (TextView)findViewById(R.id.winDrawLoseNullity);
        scoreSumDescore= (TextView)findViewById(R.id.scoreSumDescore);


    }

    public void setFCMatchHeader(ClubMainResult items){
        mItem = items;

        clubName.setText(items.clubName);
        clubFound.setText(items.clubFound);
        clubSkill.setText(Double.toString(items.clubSkill));
        clubManner.setText(Double.toString(items.clubManner));
        clubAllMatchCnt.setText(Integer.toString(items.clubAllMatchCnt));
        winDrawLoseNullity.setText(items.win + " 승  /" + items.draw + " 무 /" +
                                    items.lose +" 패 / " + items.nullity +" 무효 ");
        scoreSumDescore.setText(items.scoreSum +" 골  /" + items.deScoreSum + " 실점  ");

    }

}
