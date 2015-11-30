package com.android.ground.ground.view.fc.management;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.fc.fcmain.ClubAndMember.ClubAndMemberResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Tacademy on 2015-11-04.
 */
public class ManagementMemberItemView extends FrameLayout{

    TextView memName;
    ImageView memImage;
    public ClubAndMemberResult mItem;

    DisplayImageOptions options;

    public ManagementMemberItemView(Context context) {
        super(context);
        init();
    }
    public void init() {
        inflate(getContext(), R.layout.custom_controller_fc_management_member_list, this);
        memName = (TextView)findViewById(R.id.memName);
        memImage = (ImageView)findViewById(R.id.memImage);

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
    public void setManagementMemberItem(ClubAndMemberResult item){
        mItem = item;
        memName.setText(item.memName);
       ImageLoader.getInstance().displayImage((NetworkManager.ImageUrl + item.memImage), memImage, options);

    }

}
