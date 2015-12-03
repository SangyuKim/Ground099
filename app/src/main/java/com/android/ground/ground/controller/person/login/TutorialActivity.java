package com.android.ground.ground.controller.person.login;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.main.MainActivity;
import com.android.ground.ground.controller.person.main.MainFragment;
import com.android.ground.ground.custom.CustomDateSlider;
import com.android.ground.ground.custom.CustomMonthYearDateSlider;
import com.android.ground.ground.custom.CustomYearPicker;
import com.android.ground.ground.custom.TimeLabeler;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.log.Logger;
import com.android.ground.ground.model.login.KakaoLogin;
import com.android.ground.ground.model.usermgmt.ExtraUserPropertyLayout;
import com.android.ground.ground.model.widget.KakaoToast;
import com.android.ground.ground.model.widget.WaitingDialog;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;


/**
 * 유효한 세션이 있다는 검증 후
 * me를 호출하여 가입 여부에 따라 가입 페이지를 그리던지 Main 페이지로 이동 시킨다.
 */
public class TutorialActivity extends AppCompatActivity {
    protected static Activity self;
    Bundle mSavedInstanceState;
    public static final String TAG_SIGNUP_FRAGMENT = "5";

    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
//        requestMe();
        showSignup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.setCurrentActivity(this);
        self = TutorialActivity.this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearReferences();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());
    }
    private void clearReferences() {
        Activity currActivity = MyApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            MyApplication.setCurrentActivity(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

//    /**
//     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
//     */
//    public void requestMe() {
//        UserManagement.requestMe(new MeResponseCallback() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                String message = "failed to get user info. msg=" + errorResult;
//                Logger.d(message);
//                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
//                if (result == ErrorCode.CLIENT_ERROR_CODE) {
//                    KakaoToast.makeToast(getApplicationContext(), getString(R.string.error_message_for_service_unavailable), Toast.LENGTH_SHORT).show();
//                    finish();
//                } else {
//                    redirectLoginActivity();
//                }
//            }
//
//            @Override
//            public void onSessionClosed(ErrorResult errorResult) {
//                redirectLoginActivity();
//            }
//
//            @Override
//            public void onSuccess(UserProfile userProfile) {
//                //user 정보 저장 -> shared preference
//                Long userId = userProfile.getId();
//                //유저 정보 획득
//                Logger.d("UserProfile : " + userProfile);
//                redirectMainActivity();
//            }
//
//            @Override
//            public void onNotSignedUp() {
//
//            }
//        });
//    }
    private void redirectMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    protected static void showWaitingDialog() {
        WaitingDialog.showWaitingDialog(self);
    }

    protected static void cancelWaitingDialog() {
        WaitingDialog.cancelWaitingDialog();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, SampleLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, TutorialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


    //logout
    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                redirectLoginActivity();
            }
        });
    }
    //unlink
    private void onClickUnlink() {
        final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
        new AlertDialog.Builder(this)
                .setMessage(appendMessage)
                .setPositiveButton(getString(R.string.com_kakao_ok_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        redirectLoginActivity();
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                        redirectSignupActivity();
                                    }

                                    @Override
                                    public void onSuccess(Long result) {
                                        redirectLoginActivity();
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

    }
    Serializable mKakaoLogin;
    protected void showSignup() {

        setContentView(R.layout.activity_login);
        mKakaoLogin = getIntent().getSerializableExtra("KakaoLogin");

        if (mSavedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, TutorialFragment.newInstance("", "")).commit();
        }
//        final ExtraUserPropertyLayout extraUserPropertyLayout = (ExtraUserPropertyLayout) findViewById(R.id.extra_user_property);

    }
    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    KakaoToast.makeToast(getApplicationContext(), getString(R.string.error_message_for_service_unavailable), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.d("UserProfile : " + userProfile);
                redirectMainActivity();
            }

            @Override
            public void onNotSignedUp() {
                showSignup();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //// TODO: 2015-12-03  
        if(getSupportFragmentManager().findFragmentByTag(TAG_SIGNUP_FRAGMENT).isVisible()){
            finish();
        }
    }
}
