package com.android.ground.ground.model.login;

import java.io.Serializable;

/**
 * Created by Tacademy on 2015-11-23.
 */
public class KakaoLogin implements Serializable{
    public int code;
    public String msg;
    public KakaoLoginUser user;
}
