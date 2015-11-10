package com.android.ground.ground.view;

import android.view.View;
import android.widget.Adapter;

/**
 * Created by Tacademy on 2015-11-04.
 */
public interface OnAdapterSpecificDialogListener {
    public void onAdapterDialogClick(Adapter adapter, View view, String tag);
}
