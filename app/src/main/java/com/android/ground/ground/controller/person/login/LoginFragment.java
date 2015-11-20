package com.android.ground.ground.controller.person.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.main.MainActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
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
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


/**
 * 샘플에서 사용하게 될 로그인 페이지
 * 세션을 오픈한 후 action을 override해서 사용한다.
 *
 * @author MJ
 */
public class LoginFragment extends Fragment {
    protected static Activity self;
    private SessionCallback callback;

    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.
     *
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */


    public static final int MODE_NONE = -1;
    public static final int MODE_PROFILE = 1;
    public static final int MODE_POST = 2;
    int mode = MODE_NONE;

    Button btn, btnFacebook , btnKakao;
    CallbackManager mCallbackManager;
    LoginManager mLoginManager;
    AccessTokenTracker tracker;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        self = getActivity();
//        callback = new SessionCallback();
//        Session.getCurrentSession().addCallback(callback);
//        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
            View view =  inflater.inflate(R.layout.fragment_login, container, false);
            ((Activity)getActivity()).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity)getActivity()).getWindow().setBackgroundDrawableResource(R.drawable.xxdpi_bg);
            btnKakao = (Button)view.findViewById(R.id.button41);
            btnKakao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), SampleLoginActivity.class);
                    startActivity(intent);
                }
            });


            //// TODO: 2015-10-29
            //연동 로그인 후, 토큰 값 받기
            //토큰 값 서버에 전달
            //서버에서 가입 유무 확인
            mCallbackManager = CallbackManager.Factory.create();
            mLoginManager = LoginManager.getInstance();
            btnFacebook = (Button)view.findViewById(R.id.button10);
            setLabel();
            btnFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!isLogin()) {
                        login(Arrays.asList("email"), true);
//                        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//                            @Override
//                            public void onSuccess(LoginResult loginResult) {
//
//                            }//success
//
//                            @Override
//                            public void onCancel() {
//
//                            }
//
//                            @Override
//                            public void onError(FacebookException error) {
//
//                            }
//                        });
//                        mLoginManager.logInWithReadPermissions(LoginFragment.this, null);
                    } else {
                        mLoginManager.logOut();
                    }
                }
            });

            btn = (Button)view.findViewById(R.id.button3);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment mFragment = (Fragment) TutorialFragment.newInstance("", "");
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, mFragment)
                            .addToBackStack(null)
                            .commit();

                }
            });

            btn = (Button)view.findViewById(R.id.button4);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            return view;
//        }//카톡 세션

//        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    private void setLabel() {
        if (!isLogin()) {
            btnFacebook.setText("login");
        } else {
            btnFacebook.setText("logout");
        }
    }
    private boolean isLogin() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        return token==null?false:true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Activity activity = getActivity();
            if(activity != null){
                Intent intent = new Intent(activity, LoginActivity.class);
                intent.putExtra("kakaoSession", true);
                startActivity(intent);
                activity.finish();
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
            getActivity().setContentView(R.layout.activity_login);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Session.getCurrentSession().removeCallback(callback);
        clearReferences();
    }
    private void clearReferences() {
        Activity currActivity = MyApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            MyApplication.setCurrentActivity(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.setCurrentActivity(getActivity());
        self = getActivity();
    }
    @Override
    public void onPause() {
        super.onPause();
        clearReferences();
    }
    protected static void showWaitingDialog() {
        WaitingDialog.showWaitingDialog(self);
    }

    protected static void cancelWaitingDialog() {
        WaitingDialog.cancelWaitingDialog();
    }

//    private void postLoginFacebook(String token) {
//        NetworkManager.getInstance().postNetworkFacebook(getContext(), token);
//    }

    private void login(List<String> permissions) {
        login(permissions, true);
    }

    private void login(List<String> permissions, boolean isRead) {
        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken token = AccessToken.getCurrentAccessToken();
                //토큰 보내기
//                postLoginFacebook(token.getToken().toString());
                Log.d("hello", "facebook token  from Login Fragment: "+ token.getToken().toString() );

                Toast.makeText(getContext(), "id : " + token.getUserId(), Toast.LENGTH_SHORT).show();
                NetworkManager.getInstance().loginFacebookToken(getContext(), token.getToken(), "NOTREGISTER", new NetworkManager.OnResultListener<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result.equals("OK")) {
                            PropertyManager.getInstance().setFacebookId(token.getUserId());
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }else if (result.equals("NOTREGISTER")) {
//                                        startActivity(new Intent(getContext(), SignupActivity.class));
//                                        getActivity().finish();
                            Fragment mFragment = (Fragment) TutorialFragment.newInstance("", "");
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, mFragment)
                                    .addToBackStack(null)
                                    .commit();
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
            mLoginManager.logInWithReadPermissions(LoginFragment.this, permissions);
        } else {
            mLoginManager.logInWithPublishPermissions(LoginFragment.this, permissions);
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
                    Toast.makeText(getContext(), "error : " + message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "profile : " + object.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.executeAsync();
    }
    private void postMessage() {
        String message = "facebook test message";
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        String graphPath = "/me/feed";
        Bundle parameters = new Bundle();
        parameters.putString("message",message);
        parameters.putString("link", "http://developers.facebook.com/docs/android");
        parameters.putString("picture", "https://raw.github.com/fbsamples/.../iossdk_logo.png");
        parameters.putString("name", "Hello Facebook");
        parameters.putString("description", "The 'Hello Facebook' sample  showcases simple …");
        GraphRequest request = new GraphRequest(accessToken, graphPath, parameters, HttpMethod.POST,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        JSONObject data = response.getJSONObject();
                        String id = (data == null)?null:data.optString("id");
                        if (id == null) {
                            Toast.makeText(getContext(), "error : " + response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "post object id : " + id, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        request.executeAsync();
    }

}
