package com.android.ground.ground.controller.person.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.Tutorial.TutorialActivity;
import com.android.ground.ground.controller.person.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //// TODO: 2015-10-29
        //연동 로그인 후, 토큰 값 받기
        //토큰 값 서버에 전달
        //서버에서 가입 유무 확인

        Button btn = (Button)findViewById(R.id.button3);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, TutorialActivity.class);
                startActivity(intent);
            }
        });

        btn = (Button)findViewById(R.id.button4);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
