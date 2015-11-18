package com.android.ground.ground.custom;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Tacademy on 2015-11-18.
 */
public class CustomCircularImageView  extends CustomShaderImageView {
    private CustomcircleShader shader;

    public CustomCircularImageView(Context context) {
        super(context);
    }

    public CustomCircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public CustomShaderHelp createImageViewHelper() {
        shader = new CustomcircleShader();
        return shader;
    }

    public float getBorderRadius() {
        if(shader != null) {
            return shader.getBorderRadius();
        }
        return 0;
    }

    public void setBorderRadius(final float borderRadius) {
        if(shader != null) {
            shader.setBorderRadius(borderRadius);
            invalidate();
        }
    }
}
