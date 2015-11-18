package com.android.ground.ground.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.android.ground.ground.R;
import com.kakao.usermgmt.LoginButton;


public class CustomKakaoLoginButton extends LoginButton {
    public CustomKakaoLoginButton(Context context) {
        super(context);
    }

    public CustomKakaoLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomKakaoLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeAllViews();
        inflate(getContext(), R.layout.test_kakao_login, this);
    }
}
