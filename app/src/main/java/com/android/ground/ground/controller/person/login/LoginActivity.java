package com.android.ground.ground.controller.person.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.main.MainFragment;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, LoginFragment.newInstance("", "")).commit();
        }
    }

}
