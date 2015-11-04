package com.android.ground.ground.view;

import android.view.View;
import android.widget.Adapter;

import com.android.ground.ground.view.person.message.MyMessageItemView;

/**
 * Created by Tacademy on 2015-11-04.
 */
public interface OnAdapterReplyListener {
    public void onAdapterReplyClick(Adapter adapter, View view);
}
