package com.android.ground.ground.custom;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;

import com.android.ground.ground.view.OnProfileClickListener;

/**
 * Created by Tacademy on 2015-11-05.
 */
public class CustomDrawerLayout extends DrawerLayout {
    public CustomDrawerLayout(Context context) {
        super(context);
    }

    public CustomDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void openDrawer(View drawerView) {
        if (mDrawerListener != null) {
            mDrawerListener.onAdapterDialogClick();
        }
        super.openDrawer(drawerView);
    }

    @Override
    public void openDrawer(int gravity) {
        if (mDrawerListener != null) {
            mDrawerListener.onAdapterDialogClick();
        }
        super.openDrawer(gravity);
    }
    public interface OnDrawerListener{
        public void onAdapterDialogClick();
    }
    OnDrawerListener mDrawerListener;
    public void setOnDrawerListener(OnDrawerListener listener) {
        mDrawerListener = listener;
    }

}
