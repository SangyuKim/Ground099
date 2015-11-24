/**
 * Copyright 2014 Daum Kakao Corp.
 *
 * Redistribution and modification in source or binary forms are not permitted without specific prior written permission. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ground.ground.controller.person.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.main.MainActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.login.FacebookLogin;
import com.android.ground.ground.model.login.KakaoLogin;
import com.android.ground.ground.model.login.KakaoResponse;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageResult;
import com.android.ground.ground.model.person.profile.MyPageTrans;
import com.android.ground.ground.model.widget.WaitingDialog;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.network.request.AccessTokenInfoRequest;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * 샘플에서 사용하게 될 로그인 페이지
 * 세션을 오픈한 후 action을 override해서 사용한다.
 *
 * @author MJ
 */
public class SampleLoginActivity extends AppCompatActivity {


    public static final int MODE_NONE = -1;
    public static final int MODE_PROFILE = 1;
    public static final int MODE_POST = 2;
    int mode = MODE_NONE;

    Button btn,  btnKakao;
    ImageView btnFacebook;
    CallbackManager mCallbackManager;
    LoginManager mLoginManager;
    AccessTokenTracker tracker;

    protected static Activity self;
    private SessionCallback callback;

    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.
     *
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
            //세션이 닫혀있거나 refresh token으로 갱신 불가
            setContentView(R.layout.layout_common_kakao_login);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            this.getWindow().setBackgroundDrawableResource(R.drawable.xxdpi_bg);

            //// TODO: 2015-10-29
            //연동 로그인 후, 토큰 값 받기
            //토큰 값 서버에 전달
            //서버에서 가입 유무 확인
            mCallbackManager = CallbackManager.Factory.create();
            mLoginManager = LoginManager.getInstance();
            btnFacebook = (ImageView)findViewById(R.id.button10);
            btnFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLogin()) {
                        login(Arrays.asList("email"), true);
                        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                AccessToken token = loginResult.getAccessToken();
                                //todo
                                NetworkManager.getInstance().postNetworkLoginFacebook(SampleLoginActivity.this, token.getToken().toString(), new NetworkManager.OnResultListener<FacebookLogin>() {
                                    @Override
                                    public void onSuccess(FacebookLogin result) {
                                        if(result.user.signUpYN ==0){
                                            //signup 으로 이동
                                            final Intent intent = new Intent(SampleLoginActivity.this, TutorialActivity.class);
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
                                            final Intent intent = new Intent(SampleLoginActivity.this, MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFail(int code) {

                                    }
                                });
                                //가입이 안되었을 경우
                                redirectSignupActivity();
                                //가입이 되었을 경우 -> Main Activity
                            }//success

                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onError(FacebookException error) {

                            }
                        });
                        mLoginManager.logInWithReadPermissions(SampleLoginActivity.this, null);
                    }
//                    else {
//                        mLoginManager.logOut();
//                    }
                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
        clearReferences();

   }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            String kakaoToken = Session.getCurrentSession().getAccessToken();
            Log.d("hello", "kakao from login: " + kakaoToken);
            if(kakaoToken != null)
                sendLoginKakao(kakaoToken);

//            redirectSignupActivity();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.layout_common_kakao_login);
            //onCreate 에 있는 부분 함수화해서 옮기기
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.setCurrentActivity(this);
        self = SampleLoginActivity.this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearReferences();
    }


    private void clearReferences() {
        Activity currActivity = MyApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            MyApplication.setCurrentActivity(null);
        }
    }

    protected static void showWaitingDialog() {
        WaitingDialog.showWaitingDialog(self);
    }

    protected static void cancelWaitingDialog() {
        WaitingDialog.cancelWaitingDialog();
    }

   protected void redirectSignupActivity() {
        //세션이 살아 있을 경우
        final Intent intent = new Intent(this, TutorialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


// ===============================================================================
    private boolean isLogin() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token==null?false:true;
    }

//    private void postLoginFacebook(String token) {
//        NetworkManager.getInstance().postNetworkFacebook(getContext(), token);
//    }

    private void login(List<String> permissions, boolean isRead) {
        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken token = AccessToken.getCurrentAccessToken();
                //토큰 보내기
//                postLoginFacebook(token.getToken().toString());
                Log.d("hello", "facebook token  from Login Fragment: " + token.getToken().toString());

                Toast.makeText(MyApplication.getContext(), "id : " + token.getUserId(), Toast.LENGTH_SHORT).show();
                NetworkManager.getInstance().loginFacebookToken(MyApplication.getContext(), token.getToken(), "NOTREGISTER", new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("OK")) {
                            PropertyManager.getInstance().setFacebookId(token.getUserId());
                            startActivity(new Intent(MyApplication.getContext(), MainActivity.class));
                            finish();
                        } else if (result.equals("NOTREGISTER")) {
//                                        startActivity(new Intent(getContext(), SignupActivity.class));
//                                        getActivity().finish();


//                            Fragment mFragment = (Fragment) TutorialFragment.newInstance("", "");
//                            getFragmentManager().beginTransaction()
//                                    .replace(R.id.container, mFragment)
//                                    .addToBackStack(null)
//                                    .commit();
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

            }
        });
        if (isRead) {
            mLoginManager.logInWithReadPermissions(SampleLoginActivity.this, permissions);
        } else {
            mLoginManager.logInWithPublishPermissions(SampleLoginActivity.this, permissions);
        }
    }
    private void getProfile() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        GraphRequest request = new GraphRequest(token, "/me", null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                JSONObject object = response.getJSONObject();
                if (object == null) {
                    String message = response.getError().getErrorMessage();
                    Toast.makeText(MyApplication.getContext(), "error : " + message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyApplication.getContext(), "profile : " + object.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.executeAsync();
    }



    private void sendLoginKakao(String token) {
        NetworkManager.getInstance().postNetworkLoginKakao(SampleLoginActivity.this, token, new NetworkManager.OnResultListener<KakaoLogin>() {
            @Override
            public void onSuccess(KakaoLogin result) {
                if (result.code == 200) {
                    if (result.user == null) {
                        NetworkManager.getInstance().getNetworkLoginKakao(SampleLoginActivity.this, "", new NetworkManager.OnResultListener<KakaoResponse>() {
                            @Override
                            public void onSuccess(KakaoResponse result) {
                           }

                            @Override
                            public void onFail(int code) {

                            }
                        });
                    } else {
                        if (result.user.signUpYN == 0) {
                            //signup 으로 이동
                            PropertyManager.getInstance().setUserName(result.user.memName);
                            PropertyManager.getInstance().setUserId(result.user.id);
                            final Intent intent = new Intent(SampleLoginActivity.this, TutorialActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            intent.putExtra("KakaoLogin", result);
                            finish();
                        } else {
                            //main으로 이동
                            PropertyManager.getInstance().setUserName(result.user.memName);
                            PropertyManager.getInstance().setUserId(result.user.id);
                            searchMyPage(result.user.member_id);
                            searchMyPageTrans(result.user.member_id);
                            final Intent intent = new Intent(SampleLoginActivity.this, MainActivity.class);
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
    private void searchMyPage(final int memberId) {
        NetworkManager.getInstance().getNetworkMyPage(SampleLoginActivity.this, memberId, new NetworkManager.OnResultListener<MyPage>() {
            @Override
            public void onSuccess(MyPage result) {
                for (MyPageResult item : result.items) {
                    PropertyManager.getInstance().setMyPageResult(item);
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
    }
    private void searchMyPageTrans(final int memberId) {
        NetworkManager.getInstance().getNetworkMyPageTrans(SampleLoginActivity.this, memberId, new NetworkManager.OnResultListener<MyPageTrans>() {
            @Override
            public void onSuccess(MyPageTrans result) {
                PropertyManager.getInstance().setMyPageTransResult(result.items);
            }

            @Override
            public void onFail(int code) {
             }
        });
    }



}
