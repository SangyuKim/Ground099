package com.android.ground.ground.custom;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.android.ground.ground.R;

/**
 * Created by Tacademy on 2015-11-09.
 */
public class CustomNavigationView extends NavigationView {

    ImageView imageView;
    public CustomNavigationView(Context context) {
        this(context, null);
    }

    public CustomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View headerView = inflateHeaderView(R.layout.nav_header_main);

        headerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hello", "image clicked");
                mListener.onHeaderItemSelected();
            }
        });

    }

    public interface OnHeaderItemSelectedListener {

        /**
         * Called when an item in the navigation menu is selected.
               *
         * @return true to display the item as the selected item
         */
        public boolean onHeaderItemSelected();
    }

    /**
     * User interface state that is stored by NavigationView for implementing
     * onSaveInstanceState().
     */
    private OnHeaderItemSelectedListener mListener;
    public void setHeaderItemSelectedListener(OnHeaderItemSelectedListener listener) {
        mListener = listener;
    }

}
