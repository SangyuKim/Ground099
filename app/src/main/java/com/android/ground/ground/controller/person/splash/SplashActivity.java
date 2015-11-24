package com.android.ground.ground.controller.person.splash;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.http.RequestQueue;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.RegistrationIntentService;
import com.android.ground.ground.controller.person.login.TutorialActivity;
import com.android.ground.ground.controller.person.login.SampleLoginActivity;
import com.android.ground.ground.controller.person.main.MainActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.manager.ServerUtilities;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.login.FacebookLogin;
import com.android.ground.ground.model.login.KakaoLogin;
import com.android.ground.ground.model.login.KakaoResponse;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageResult;
import com.android.ground.ground.model.person.profile.MyPageTrans;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.IOException;

import com.kakao.auth.ISessionCallback;

/**
 * 샘플에서 사용하게 될 로그인 페이지
 * 세션을 오픈한 후 action을 override해서 사용한다.
 *
 * @author MJ
 */
public class SplashActivity extends AppCompatActivity {



    protected static Activity self;

    static final private String TAG = "GCM_Example";
    static final private String serverAddress = "http://192.168.0.105:3000";

    private Handler handler;
    private TextView registrationIDLabel;
    private TextView deviceIDLabel;
    private RequestQueue mQueue;

    private String deviceID;
    private String registrationID;
    private String deviceOS = "Android";


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    Handler mHandler = new Handler(Looper.getMainLooper());
    CallbackManager callbackManager = CallbackManager.Factory.create();
    LoginManager mLoginManager = LoginManager.getInstance();
    AccessTokenTracker mTokenTracker;

    private SessionCallback callback;

    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.
     *
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Button btnLogout = (Button)findViewById(R.id.button49);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                    }
                });
            }
        });

        //unlink
//        final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
//        new AlertDialog.Builder(this)
//                .setMessage(appendMessage)
//                .setPositiveButton(getString(R.string.com_kakao_ok_button),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                UserManagement.requestUnlink(new UnLinkResponseCallback() {
//                                    @Override
//                                    public void onFailure(ErrorResult errorResult) {
//                                    }
//
//                                    @Override
//                                    public void onSessionClosed(ErrorResult errorResult) {
//
//                                    }
//
//                                    @Override
//                                    public void onNotSignedUp() {
//
//                                    }
//
//                                    @Override
//                                    public void onSuccess(Long result) {
//
//                                    }
//                                });
//                                dialog.dismiss();
//                            }
//                        })
//                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).show();



//        postLogin();

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, SampleLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
//---------------------------------------KakaoTalk---------------------------------------------------


        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        if (!Session.getCurrentSession().checkAndImplicitOpen()) {

//---------------------------------------Facebook----------------------------------------------------
            final String id = PropertyManager.getInstance().getFaceBookId();
            if (!TextUtils.isEmpty(id)) {
                // facebook login
                mTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                        AccessToken token = AccessToken.getCurrentAccessToken();
                        if (token != null) {
                            if (token.getUserId().equals(id)) {
//                                NetworkManager.getInstance().loginFacebookToken(SplashActivity.this, token.getToken(),"OK", new NetworkManager.OnResultListener<String>() {
//                                    @Override
//                                    public void onSuccess(String result) {
//                                        if (result.equals("OK")) {
//                                            goMainActivity();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFail(int code) {
//
//                                    }
//                                });

                                //todo
                                NetworkManager.getInstance().postNetworkLoginFacebook(SplashActivity.this, token.getToken().toString()
                                        , new NetworkManager.OnResultListener<FacebookLogin>() {
                                    @Override
                                    public void onSuccess(FacebookLogin result) {
                                        if(result.user.signUpYN ==0){
                                            //signup 으로 이동
                                            final Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
//                                            PropertyManager.getInstance().setUserId(result.user.id);
//                                            PropertyManager.getInstance().setUserName(result.user.memName);
                                            finish();
                                        }else{
                                            //main으로 이동
//                                            PropertyManager.getInstance().setUserId(result.user.id);
//                                            PropertyManager.getInstance().setUserName(result.user.memName);
                                            searchMyPage(result.user.member_id);
                                            searchMyPageTrans(result.user.member_id);
                                            final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFail(int code) {

                                    }
                                });
                            } else {
                                Toast.makeText(SplashActivity.this, "facebook id change", Toast.LENGTH_SHORT).show();
                                mLoginManager.logOut();
                                goLoginActivity();
                            }
                        }
                    }
                };
                mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                       AccessToken token = loginResult.getAccessToken();
                        //todo
                        NetworkManager.getInstance().postNetworkLoginFacebook(SplashActivity.this, token.getToken().toString(), new NetworkManager.OnResultListener<FacebookLogin>() {
                            @Override
                            public void onSuccess(FacebookLogin result) {
                                if(result.user.signUpYN ==0){
                                    //signup 으로 이동
                                    final Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
//                                    PropertyManager.getInstance().setUserId(result.user.id);
//                                    PropertyManager.getInstance().setUserName(result.user.memName);
                                    finish();
                                }else{
                                    //main으로 이동
//                                    PropertyManager.getInstance().setUserId(result.user.id);
//                                    PropertyManager.getInstance().setUserName(result.user.memName);
                                    searchMyPage(result.user.member_id);
                                    searchMyPageTrans(result.user.member_id);
                                    final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                    finish();
                                }
                            }

                            @Override
                            public void onFail(int code) {

                            }
                        });
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(SplashActivity.this, "facebook login fail...",Toast.LENGTH_SHORT).show();
                        goLoginActivity();
                    }
                });
                mLoginManager.logInWithReadPermissions(this, null);
            }
            else {

                //todo

// =================================================== 실행하기

//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        goLoginActivity();
//                    }
//                }, 2000);
            }


//----------------------------------------GCM--------------------------------------------------------
            registrationIDLabel = (TextView) findViewById(R.id.tokenLabel);

//            mQueue = Volley.newRequestQueue(this);
            handler = new Handler();

            Button checkButton = (Button)findViewById(R.id.checkPlayServiceButton);
            checkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPlayService();
                }
            });

            // 토큰 발급 버튼과 이벤트
            Button requestTokenButton = (Button)findViewById(R.id.requestDeviceTokenButton);
            requestTokenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // API19 에서 에러
//            requestDeviceToken();
                    new RequestTokenThread().start();
                }
            });

            Button deviceIDButton = (Button)findViewById(R.id.getDeviceIDButton);
            deviceIDLabel = (TextView)findViewById(R.id.deviceIdLabel);
            deviceIDButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resolveDeviceID();
                }
            });

            // 토큰 등록 버튼과 이벤트
            Button registTokenButton = (Button)findViewById(R.id.registTokenButton);
            registTokenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Register Device Token to App Server");
                    registerToken();
                }
            });

            TextView addressView = (TextView)findViewById(R.id.serverAddress);
            addressView.setText("Server Address : " + serverAddress);

            showStoredToken();


            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    doRealStart();
                }
            };
            setUpIfNeeded();

        }//카톡 세션 확인

        AccessToken token = AccessToken.getCurrentAccessToken();
        if(token != null) {
//            sendLoginFacebook(token.getToken().toString());
            Log.d("hello", "facebook token  from SplashActivity: " + token.getToken().toString());
            NetworkManager.getInstance().postNetworkLoginFacebook(SplashActivity.this, token.getToken().toString(), new NetworkManager.OnResultListener<FacebookLogin>() {
                @Override
                public void onSuccess(FacebookLogin result) {
                    if (result.user.signUpYN == 0) {
                        //signup 으로 이동
                        final Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
//                        PropertyManager.getInstance().setUserId(result.user.id);
//                                    PropertyManager.getInstance().setUserName(result.user.memName);
                        finish();
                    } else {
                        //main으로 이동
//                        PropertyManager.getInstance().setUserId(result.user.id);
//                                    PropertyManager.getInstance().setUserName(result.user.memName);
                        searchMyPage(result.user.member_id);
                        searchMyPageTrans(result.user.member_id);
                        final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFail(int code) {

                }
            });
        }



    }

    private void resolveDeviceID() {
        String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceIDLabel.setText(androidID);
        Log.d(TAG, "Android ID : " + androidID);

        deviceID = androidID;
    }

    // 플레이 서비스 사용 가능 여부 체크
    void checkPlayService() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        Log.d(TAG, "isGooglePlayServicesAvailable : " + resultCode);
        if (ConnectionResult.SUCCESS == resultCode) {
            // 구글 플레이 서비스 가능
            Toast.makeText(this, "플레이 서비스 사용 가능", Toast.LENGTH_SHORT).show();
        } else {
            // 구글 플레이 서비스 불가능 - 설치/업데이트 다이얼로그 출력
            GoogleApiAvailability.getInstance().getErrorDialog(this, resultCode, 0).show();
        }
    }
    // Registration Intent Service를 이용해서 토큰 발급 받기

    void requestDeviceToken() {
        // 토큰 발급 브로드캐스트 리시버
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String token = intent.getExtras().getString("TOKEN");
                if ( registrationID != token ) {
                    registrationID = token;
                    saveRegistrationID();
                }

                registrationIDLabel.setText(token);
            }
        }, new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE_BROADCAST));

        Log.d(TAG, "start registration service");

        // 토큰 발급 서비스 시작
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }

    // 토큰 직접 발급받기 - IntentService로 작성하는 것을 권장
    class RequestTokenThread extends Thread {
        @Override
        public void run() {
            Log.d(TAG, "Trying to regist device");
            try {
                InstanceID instanceID = InstanceID.getInstance(SplashActivity.this);
                final String token = instanceID.getToken(getString(R.string.GCM_SenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                if ( registrationID != token ) {
                    registrationID = token;
                    saveRegistrationID();
                }

                Log.d(TAG, "Token : " + token);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        registrationIDLabel.setText(token);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Regist Exception", e);
            }
        }
    }

    void showStoredToken() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String storedToken = sharedPref.getString("REGISTRATION_ID", null);
        if ( storedToken != null ) {
            registrationID = storedToken;
            registrationIDLabel.setText(registrationID);
            Log.d("token: ", registrationID);
        }
    }

    void saveRegistrationID() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("REGISTRATION_ID", registrationID);
        editor.commit();
    }

    void registerToken() {
        // Device ID가 없으면 발급
        if ( deviceID == null )
            resolveDeviceID();

//        StringRequest request = new StringRequest(Request.Method.POST, serverAddress + "/register", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "Token 등록 결과  : " + response);
//                Toast.makeText(SplashActivity.this, "Token 등록 성공", Toast.LENGTH_SHORT).show();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error", error);
//                NetworkResponse response = error.networkResponse;
//                if ( response != null ) {
//                    Log.e(TAG, "Error Response : " + response.statusCode);
//                    Toast.makeText(SplashActivity.this, "Token 등록 에러 " + response.toString(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                // 바디 작성
//                Map<String, String> params = new HashMap<>();
//                params.put("deviceID", deviceID);
//                params.put("token", registrationID);
//                params.put("os", deviceOS);
//
//                return params;
//            }
//
//            @Override
//            public String getBodyContentType() {
//                // 컨텐트 타입
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//        };
//        mQueue.add(request);
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE));
        MyApplication.setCurrentActivity(this);
        self = SplashActivity.this;
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
        clearReferences();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLAY_SERVICES_RESOLUTION_REQUEST &&
                resultCode == Activity.RESULT_OK) {
            setUpIfNeeded();
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpIfNeeded() {
        if (checkPlayServices()) {
            String regId = PropertyManager.getInstance().getRegistrationToken();
            if (!regId.equals("")) {
                doRealStart();
            } else {
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    private void doRealStart() {
        // activity start...
        new AsyncTask<Void,Void,Boolean>(){
            @Override
            protected Boolean doInBackground(Void... params) {
                String regid = PropertyManager.getInstance().getRegistrationToken();
                ServerUtilities.register(SplashActivity.this, regid);
                return null;
            }
        }.execute();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Dialog dialog = apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog.show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }
    private void searchMyPage(final int memberId) {
        NetworkManager.getInstance().getNetworkMyPage(SplashActivity.this, memberId, new NetworkManager.OnResultListener<MyPage>() {
            @Override
            public void onSuccess(MyPage result) {
                for (MyPageResult item : result.items) {
                    PropertyManager.getInstance().setMyPageResult(item);
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(SplashActivity.this, "선수 정보 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void searchMyPageTrans(final int memberId) {
        NetworkManager.getInstance().getNetworkMyPageTrans(SplashActivity.this, memberId, new NetworkManager.OnResultListener<MyPageTrans>() {
            @Override
            public void onSuccess(MyPageTrans result) {
                PropertyManager.getInstance().setMyPageTransResult(result.items);
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(SplashActivity.this, "선수 정보 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void goMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void goLoginActivity() {
        startActivity(new Intent(this, SampleLoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
        if (mTokenTracker != null) {
            mTokenTracker.stopTracking();
        }
        Session.getCurrentSession().removeCallback(callback);

    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            //todo
            //카톡 토큰 얻기 -> 서버에서 확인 작업 -> 선수 아이디 얻기

            String kakaoToken = Session.getCurrentSession().getAccessToken();
            Log.d("hello", "kakao from Splash: " + kakaoToken);
            if(kakaoToken != null)
                sendLoginKakao(kakaoToken);

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {

            if(exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.layout_common_kakao_login);
        }
    }


       private void clearReferences() {
        Activity currActivity = MyApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            MyApplication.setCurrentActivity(null);
        }
    }

    private void redirectSignupFragment() {
        Intent intent =new Intent(SplashActivity.this, SampleLoginActivity.class);
//        intent.putExtra("kakaoSession", true);
        startActivity(intent);
        finish();
    }

//    private void postLoginFacebook(String token) {
//        NetworkManager.getInstance().postNetworkFacebook(SplashActivity.this, token);
//    }
//    private void sendLoginFacebook(String token) {
//        NetworkManager.getInstance().postNetworkLoginFacebook(SplashActivity.this, token);
//    }
    private void sendLoginKakao(String token) {
        NetworkManager.getInstance().postNetworkLoginKakao(SplashActivity.this, token, new NetworkManager.OnResultListener<KakaoLogin>() {
            @Override
            public void onSuccess(KakaoLogin result) {
                if(result.code==200){
                    if(result.user == null){
                        NetworkManager.getInstance().getNetworkLoginKakao(SplashActivity.this, "", new NetworkManager.OnResultListener<KakaoResponse>() {
                            @Override
                            public void onSuccess(KakaoResponse result) {

                            }

                            @Override
                            public void onFail(int code) {

                            }
                        });
                    }else{
                        if(result.user.signUpYN ==0){
                            //signup 으로 이동
                            final Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            PropertyManager.getInstance().setUserId(result.user.id);
                            PropertyManager.getInstance().setUserName(result.user.memName);
                            finish();
                        }else{
                            //main으로 이동
                            PropertyManager.getInstance().setUserId(result.user.id);
                            PropertyManager.getInstance().setUserName(result.user.memName);
                            searchMyPage(result.user.member_id);
                            searchMyPageTrans(result.user.member_id);
                            final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        }
                    }

                }
            }

            @Override
            public void onFail(int code) {

            }
        });
    }




}
