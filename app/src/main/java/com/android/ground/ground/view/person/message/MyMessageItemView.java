package com.android.ground.ground.view.person.message;

import android.content.Context;
import android.widget.FrameLayout;

import com.android.ground.ground.R;
import com.android.ground.ground.model.person.message.MyMessageItem;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class MyMessageItemView extends FrameLayout {
    public MyMessageItemView(Context context) {
        super(context);
        init();
    }
    public void init(){
        inflate(getContext(), R.layout.custom_controller_person_message1, this);
    }
    public void setMyMessageItem(MyMessageItem item){

    }
}
