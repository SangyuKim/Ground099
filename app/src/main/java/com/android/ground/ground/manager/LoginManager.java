package com.android.ground.ground.manager;

/**
 * Created by Tacademy on 2015-11-05.
 */
public class LoginManager {

    private static LoginManager instance;
    public static LoginManager getInstance() {
        if (instance == null) {
            instance = new LoginManager();
        }
        return instance;
    }
    int isManager;
    private LoginManager(){

    }
}
