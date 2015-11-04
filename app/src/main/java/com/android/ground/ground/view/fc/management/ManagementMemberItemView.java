package com.android.ground.ground.view.fc.management;

import android.content.Context;
import android.widget.FrameLayout;

import com.android.ground.ground.R;
import com.android.ground.ground.model.fc.management.ManagementMemberItem;

/**
 * Created by Tacademy on 2015-11-04.
 */
public class ManagementMemberItemView extends FrameLayout{
    public ManagementMemberItemView(Context context) {
        super(context);
        init();
    }
    public void init() {
        inflate(getContext(), R.layout.custom_controller_fc_management_member_list, this);

    }
    public void setManagementMemberItem(ManagementMemberItem item){

    }
}
