package com.android.ground.ground.controller.person.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.etc.Area.AreaSearchActivity;
import com.android.ground.ground.controller.person.main.MainActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.etc.EtcData;
import com.android.ground.ground.model.etc.EtcData2;
import com.android.ground.ground.model.login.SignupData;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageResult;
import com.android.ground.ground.model.person.profile.MyPageTrans;
import com.android.ground.ground.model.post.signup.UserProfile;
import com.android.ground.ground.model.widget.WaitingDialog;
import com.facebook.login.LoginManager;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


/**
 * 유효한 세션이 있다는 검증 후
 * me를 호출하여 가입 여부에 따라 가입 페이지를 그리던지 Main 페이지로 이동 시킨다.
 */
public class SignupFragment extends Fragment {
    static final int MONTHYEARDATESELECTOR_ID = 3;
    ScrollView mScrollView;
    UserProfile mUserProfile;
    RadioGroup gender;
    CheckBox memMainDay_Mon,memMainDay_Tue,memMainDay_Wed,memMainDay_Thu,memMainDay_Fri,memMainDay_Sat,memMainDay_Sun;
    EditText memIntro;

    protected static Activity self;
    TextView textViewUserArea, textViewAge, memName;

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


    @Override
    public void onDestroy() {
        super.onDestroy();
        clearReferences();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());
    }

    private void clearReferences() {
        Activity currActivity = MyApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            MyApplication.setCurrentActivity(null);
        }
    }


    ImageView imageView;
    public static final int REQUEST_CODE_CROP = 0;
    File mSavedFile;
    EditText editText;
    ScrollView scrollView;
    Spinner spinner;
    View view;
    MySpinnerSignupAdapter mySpinnerAdapter;

    public static final int REQ_AREA_SEARCH = 1;
    final String[] items = new String[]{"사진 앨범 ",  "카메라"};

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
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Main으로 넘길지 가입 페이지를 그릴지 판단하기 위해 me를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        requestMe();
    }

    LinearLayout linearLayoutAge;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((Activity)getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity)getActivity()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        view = inflater.inflate(R.layout.fragment_signup, container, false);
        mUserProfile = new UserProfile();

        setSpinner(R.id.spinner_ability, R.array.items_player_ability);
        setSpinner(R.id.spinner_position, R.array.items_player_position);
        textViewUserArea = (TextView)view.findViewById(R.id.textViewArea);
        linearLayoutAge = (LinearLayout)view.findViewById(R.id.linearLayoutAge);
        mScrollView   = (ScrollView)view.findViewById(R.id.scrollView_signup);
        gender = (RadioGroup)view.findViewById(R.id.gender);
        memMainDay_Mon = (CheckBox)view.findViewById(R.id.memMainDay_Mon);
        memMainDay_Tue = (CheckBox)view.findViewById(R.id.memMainDay_Tue);
        memMainDay_Wed = (CheckBox)view.findViewById(R.id.memMainDay_Wed);
        memMainDay_Thu = (CheckBox)view.findViewById(R.id.memMainDay_Thu);
        memMainDay_Fri = (CheckBox)view.findViewById(R.id.memMainDay_Fri);
        memMainDay_Sat = (CheckBox)view.findViewById(R.id.memMainDay_Sat);
        memMainDay_Sun = (CheckBox)view.findViewById(R.id.memMainDay_Sun);
        textViewAge = (TextView)view.findViewById(R.id.textView89);
        memName = (TextView)view.findViewById(R.id.memName);

       if(PropertyManager.getInstance().getUserName() != null)
            memName.setText(PropertyManager.getInstance().getUserName());

        memIntro = (EditText)view.findViewById(R.id.memIntro);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == 0) {
                    mUserProfile.gender = 0;
                } else {
                    mUserProfile.gender = 1;
                }
            }
        });

        linearLayoutAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().showDialog(MONTHYEARDATESELECTOR_ID);
                RelativeLayout linearLayout = new RelativeLayout(getContext());
                final NumberPicker aNumberPicker = new NumberPicker(getContext());
                //올해 년도로 넣기
                aNumberPicker.setMaxValue(2015);
                aNumberPicker.setMinValue(1940);
                aNumberPicker.setValue(2015);


                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
                RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

                linearLayout.setLayoutParams(params);
                linearLayout.addView(aNumberPicker,numPicerParams);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setTitle("Select the number");
                alertDialogBuilder.setView(linearLayout);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
//                                        Log.e("hello","New Quantity Value : "+ aNumberPicker.getValue());
                                        mUserProfile.age = aNumberPicker.getValue();
                                        textViewAge.setText(Integer.toString(aNumberPicker.getValue()));

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });



        Button btn = (Button)view.findViewById(R.id.button_complete);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //유저가 사진 입력하지 않았을 때 처리하기 !!
                mUserProfile.updateYN =0;
                if(mSavedFile!=null) {
                    mUserProfile.mFile = mSavedFile;
                }else{
                    mUserProfile.mFile =  new File(Environment.getExternalStorageDirectory(),"temp_" + System.currentTimeMillis()/1000);
                }
                if( mUserProfile.gender == 0)
                    mUserProfile.gender =0;



                mUserProfile.memLocationName =textViewUserArea.getText().toString();

                if(memMainDay_Mon.isChecked()){
                    mUserProfile.memMainDay_Mon=1;
                }else{
                    mUserProfile.memMainDay_Mon=0;
                }
                if(memMainDay_Tue.isChecked()){
                    mUserProfile.memMainDay_Tue=1;
                }else{
                    mUserProfile.memMainDay_Tue=0;
                }
                if(memMainDay_Wed.isChecked()){
                    mUserProfile.memMainDay_Wed=1;
                }else{
                    mUserProfile.memMainDay_Wed=0;
                }
                if(memMainDay_Thu.isChecked()){
                    mUserProfile.memMainDay_Thu=1;
                }else{
                    mUserProfile.memMainDay_Thu=0;
                }
                if(memMainDay_Fri.isChecked()){
                    mUserProfile.memMainDay_Fri=1;
                }else{
                    mUserProfile.memMainDay_Fri=0;
                }
                if(memMainDay_Sat.isChecked()){
                    mUserProfile.memMainDay_Sat=1;
                }else{
                    mUserProfile.memMainDay_Sat=0;
                }
                if(memMainDay_Sun.isChecked()){
                    mUserProfile.memMainDay_Sun=1;
                }else{
                    mUserProfile.memMainDay_Sun=0;
                }

                mUserProfile.memIntro=memIntro.getText().toString();


                if (mUserProfile.memLocationName.equals("지역 검색")){
                    Toast.makeText(getContext(), "지역을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else if(mUserProfile.age == 0){
                    Toast.makeText(getContext(), "나이를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    NetworkManager.getInstance().postNetworkSignup(getContext(), mUserProfile, new NetworkManager.OnResultListener<SignupData>() {
                        @Override
                        public void onSuccess(SignupData result) {
                            if(result.code==200){
                                searchMyPage(PropertyManager.getInstance().getUserId());
                            }else if(result.code>=400 && result.code<500){
                                try{
                                    Toast.makeText(getContext(),result.msg , Toast.LENGTH_SHORT);
                                }catch(NullPointerException e){
                                    e.printStackTrace();
                                }

                                if(result.code == 401){
                                    try {

                                        UserManagement.requestLogout(new LogoutResponseCallback() {
                                            @Override
                                            public void onCompleteLogout() {
                                                NetworkManager.getInstance().postNetworkMemberLogout(getContext(), new NetworkManager.OnResultListener<EtcData2>() {
                                                    @Override
                                                    public void onSuccess(EtcData2 result) {
                                                        Intent intent = new Intent(getContext(), SampleLoginActivity.class);
                                                        getActivity().finish();
                                                        startActivity(intent);
                                                    }

                                                    @Override
                                                    public void onFail(int code) {

                                                    }
                                                });
                                            }
                                        });

                                        LoginManager mLoginManager = LoginManager.getInstance();
                                        if(mLoginManager != null){
                                            mLoginManager.logOut();
                                            NetworkManager.getInstance().postNetworkMemberLogout(getContext(), new NetworkManager.OnResultListener<EtcData2>() {
                                                @Override
                                                public void onSuccess(EtcData2 result) {
                                                    Intent intent = new Intent(getContext(), SampleLoginActivity.class);
                                                    getActivity().finish();
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onFail(int code) {

                                                }
                                            });
                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }
                            }else if(result.code>=500 && result.code<600){
                                try{
                                    Toast.makeText(getContext(),"서버 연결이 좋지 않습니다." , Toast.LENGTH_SHORT);
                                }catch(NullPointerException e){
                                    e.printStackTrace();
                                }
                            }else{
                                try{
                                    Toast.makeText(getContext(),result.msg , Toast.LENGTH_SHORT);
                                }catch(NullPointerException e){
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFail(int code) {
//                        Toast.makeText(MyApplication.getContext(), "error code in signup: " + code, Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
        LinearLayout areaBtn = (LinearLayout)view.findViewById(R.id.button_area_search);
        areaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "지역 검색으로 이동", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), AreaSearchActivity.class);
                startActivityForResult(intent, REQ_AREA_SEARCH);
            }
        });
        memIntro.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //// TODO: 2015-12-03  
//                if(actionId == EditorInfo.IME_ACTION_DONE){
//                    MyApplication.getmIMM().hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
                return true;
            }
        });
        imageView = (ImageView)view.findViewById(R.id.memImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.icon);
                builder.setTitle("사진 선택");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent photoPickerIntent = new Intent(
                                    Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            photoPickerIntent.setType("image/*");
                            photoPickerIntent.putExtra("crop", "true");
                            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
                            photoPickerIntent.putExtra("outputFormat",
                                    Bitmap.CompressFormat.JPEG.toString());
                            startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP);
                        } else {
                            Intent photoPickerIntent = new Intent(
                                    MediaStore.ACTION_IMAGE_CAPTURE);
                            photoPickerIntent.putExtra("crop", "circle");
                            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
                            photoPickerIntent.putExtra("outputFormat",
                                    Bitmap.CompressFormat.JPEG.toString());
                            startActivityForResult(photoPickerIntent, REQUEST_CODE_CROP);
                        }


                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        if (savedInstanceState != null) {
            String file = savedInstanceState.getString("filename");
            if (file != null) {
                mSavedFile = new File(file);
            }
        }
        return view;
    }

    private void setSpinner(int id, int dataId) {

        spinner = (Spinner)view.findViewById(id);
        mySpinnerAdapter = new MySpinnerSignupAdapter(getContext());
        spinner.setAdapter(mySpinnerAdapter);
        if(id == R.id.spinner_position) {
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "position : " + position, Toast.LENGTH_SHORT).show();
                    mUserProfile.position = position+1;

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "position : " + position, Toast.LENGTH_SHORT).show();
                    mUserProfile.skill = position+1;
                    ;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        initSpinnerData(dataId, mySpinnerAdapter);
    }

    private void initSpinnerData(int id, MySpinnerSignupAdapter mSpinnerAdapter) {

        String[] items = getResources().getStringArray(id);
        for (String s : items) {
            mSpinnerAdapter.add(s);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CROP && resultCode == Activity.RESULT_OK) {
            Bitmap bm = BitmapFactory.decodeFile(mSavedFile.getAbsolutePath());
            imageView.setImageBitmap(bm);
        }
        if(requestCode == REQ_AREA_SEARCH && resultCode == Activity.RESULT_OK){
            String userArea = data.getExtras().getString("userArea");
            double latitude = data.getExtras().getDouble("latitude");
            mUserProfile.latitude = latitude;
            double longitude = data.getExtras().getDouble("longitude");
            mUserProfile.longitude = longitude;
            textViewUserArea.setText(userArea);
        }

    }
    private Uri getTempUri() {
        mSavedFile = new File(Environment.getExternalStorageDirectory(),"temp_" + System.currentTimeMillis()/1000);

        return Uri.fromFile(mSavedFile);
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSavedFile != null) {
            outState.putString("filename", mSavedFile.getAbsolutePath());
        }
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
    private void searchMyPage(final int memberId) {
        NetworkManager.getInstance().getNetworkMyPage(getContext(), memberId, new NetworkManager.OnResultListener<MyPage>() {
            @Override
            public void onSuccess(MyPage result) {
                for (MyPageResult item : result.items) {
                    PropertyManager.getInstance().setMyPageResult(item);
                    searchMyPageTrans(memberId);

                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(MyApplication.getContext(), "선수 정보 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void searchMyPageTrans(final int memberId) {
//        showWaitingDialog();
        NetworkManager.getInstance().getNetworkMyPageTrans(getContext(), memberId, new NetworkManager.OnResultListener<MyPageTrans>() {
            @Override
            public void onSuccess(MyPageTrans result) {
//                unShowWaitingDialog();
                NetworkManager.getInstance().postNetworkMemberPush(getContext(), PropertyManager.getInstance().getDeviceId(), PropertyManager.getInstance().getRegistrationToken(), new NetworkManager.OnResultListener<EtcData>() {
                    @Override
                    public void onSuccess(EtcData result) {
                        final Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onFail(int code) {

                    }
                });
                PropertyManager.getInstance().setMyPageTransResult(result.items);

            }

            @Override
            public void onFail(int code) {
//                Toast.makeText(SplashActivity.this, "선수 정보 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
//                unShowWaitingDialog();
            }
        });
    }


    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
//    protected void requestMe() {
//        UserManagement.requestMe(new MeResponseCallback() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                String message = "failed to get user info. msg=" + errorResult;
//                Logger.d(message);
//
//                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
//                if (result == ErrorCode.CLIENT_ERROR_CODE) {
//                    KakaoToast.makeToast(MyApplication.getContext(), getString(R.string.error_message_for_service_unavailable), Toast.LENGTH_SHORT).show();
//                    getActivity().finish();
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
//                Logger.d("UserProfile : " + userProfile);
//                redirectMainActivity();
//            }
//
//            @Override
//            public void onNotSignedUp() {
////                showSignup();
//            }
//        });
//    }

    private void redirectMainActivity() {
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }

    protected static void showWaitingDialog() {
        WaitingDialog.showWaitingDialog(self);
    }

    protected static void cancelWaitingDialog() {
        WaitingDialog.cancelWaitingDialog();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(getContext(), SampleLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        getActivity().finish();
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(getContext(), TutorialActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        getActivity().finish();
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
        new AlertDialog.Builder(getContext())
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


}
