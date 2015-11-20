package com.android.ground.ground.custom;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.ground.ground.R;

/**
 * Created by Tacademy on 2015-11-20.
 */
public class CustomRoundCornerProgressBar extends CustomBaseRoundCornerProgressBar {
    public CustomRoundCornerProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRoundCornerProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int initLayout() {
        return R.layout.custom_layout_round_corner_progress_bar;
    }

    @Override
    protected void initStyleable(Context context, AttributeSet attrs) {

    }

    @Override
    protected void initView() {

    }

    @SuppressWarnings("deprecation")
    @Override
    protected void drawProgress(LinearLayout layoutProgress, float max, float progress, float totalWidth,
                                int radius, int padding, int colorProgress, boolean isReverse) {
        GradientDrawable backgroundDrawable = createGradientDrawable(colorProgress);
        int newRadius = radius - (padding / 2);
        backgroundDrawable.setCornerRadii(new float[]{newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius, newRadius});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutProgress.setBackground(backgroundDrawable);
        } else {
            layoutProgress.setBackgroundDrawable(backgroundDrawable);
        }

        float ratio = max / progress;
        int progressWidth = (int) ((totalWidth - (padding * 2)) / ratio);
        ViewGroup.LayoutParams progressParams = layoutProgress.getLayoutParams();
        progressParams.width = progressWidth;
        layoutProgress.setLayoutParams(progressParams);
    }

    @Override
    protected void onViewDraw() {

    }

}
