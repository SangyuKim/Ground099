package com.android.ground.ground.view;

import android.view.View;

import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualResResult;

/**
 * Created by Tacademy on 2015-11-04.
 */
public interface OnAdapterCustomTouchListener {
    public void onTouch(View view, LineupVirtualResResult mItem);
}
