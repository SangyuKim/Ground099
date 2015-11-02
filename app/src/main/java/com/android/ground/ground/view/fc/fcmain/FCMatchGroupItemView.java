package com.android.ground.ground.view.fc.fcmain;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.ground.ground.model.fc.fcmain.FCMatchGroupItem;
import com.android.ground.ground.R;

/**
 * Created by dongja94 on 2015-10-06.
 */
public class FCMatchGroupItemView extends FrameLayout {


    public FCMatchGroupItemView(Context context) {
        super(context);
        init();
    }
    TextView nameView;
    private void init() {
        inflate(getContext(), R.layout.custom_controller_fc_fcmain_matchhistory_group, this);
        nameView = (TextView)findViewById(R.id.textView_fcmatch_group);
    }

    public void setGroupItem(FCMatchGroupItem item) {
        nameView.setText(item.groupName);
    }


}
