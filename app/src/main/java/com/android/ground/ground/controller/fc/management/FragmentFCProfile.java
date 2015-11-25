package com.android.ground.ground.controller.fc.management;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.etc.Area.AreaSearchActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.etc.EtcData;
import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMain;
import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMainResult;
import com.android.ground.ground.model.post.fcCreate.ClubProfile;
import com.android.ground.ground.model.post.fcUpdate.ClubProfileUpdate;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFCProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFCProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFCProfile extends Fragment {

    ClubProfileUpdate mClubProfileUpdate;

    public final static String ImageUrl ="https://s3-ap-northeast-1.amazonaws.com/";
    DisplayImageOptions options;
    EditText clubIntro, clubField;
    TextView clubLocationName, clubName;
    CheckBox clubMainDay_Mon, clubMainDay_Tue, clubMainDay_Wed,clubMainDay_Thu ,clubMainDay_Fri,
            clubMainDay_Sat,clubMainDay_Sun;
    Switch memYN, matchYN, fieldYN;
    ImageView clubImage;

    ClubMainResult mItem;


    int clubId;
    public static final int REQUEST_CODE_CROP = 0;
    File mSavedFile;
    public static final int REQ_AREA_SEARCH = 1;
    final String[] items = new String[]{"사진 앨범 ",  "카메라"};

    TextView a;

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
     * @return A new instance of fragment FragmentFCProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFCProfile newInstance(String param1, String param2) {
        FragmentFCProfile fragment = new FragmentFCProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentFCProfile() {
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
        View view =inflater.inflate(R.layout.fragment_fragment_fcprofile, container, false);

        mClubProfileUpdate = new ClubProfileUpdate();
        clubId = PropertyManager.getInstance().getMyPageResult().club_id;
        clubImage = (ImageView)view.findViewById(R.id.clubImage);

        clubName = (TextView)view.findViewById(R.id.clubName);
        clubIntro = (EditText)view.findViewById(R.id.clubIntro);
        clubField = (EditText)view.findViewById(R.id.clubField);

        clubLocationName = (TextView)view.findViewById(R.id.clubLocationName);

        clubMainDay_Mon= (CheckBox)view.findViewById(R.id.clubMainDay_Mon);
        clubMainDay_Tue= (CheckBox)view.findViewById(R.id.clubMainDay_Tue);
        clubMainDay_Wed= (CheckBox)view.findViewById(R.id.clubMainDay_Wed);
        clubMainDay_Thu= (CheckBox)view.findViewById(R.id.clubMainDay_Thu);
        clubMainDay_Fri= (CheckBox)view.findViewById(R.id.clubMainDay_Fri);
        clubMainDay_Sat= (CheckBox)view.findViewById(R.id.clubMainDay_Sat);
        clubMainDay_Sun= (CheckBox)view.findViewById(R.id.clubMainDay_Sun);

        memYN= (Switch)view.findViewById(R.id.memYN);
        matchYN= (Switch)view.findViewById(R.id.matchYN);
        fieldYN= (Switch)view.findViewById(R.id.fieldYN);

        searchHeaderFCMember();

        LinearLayout areaSearch = (LinearLayout)view.findViewById(R.id.button_area_search);
        areaSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AreaSearchActivity.class);
                startActivityForResult(intent, REQ_AREA_SEARCH);
            }
        });

        Button btn = (Button)view.findViewById(R.id.button17);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //유저가 사진 입력하지 않았을 때 처리하기 !!

                if(mSavedFile!=null) {
                    mClubProfileUpdate.file = mSavedFile;
                }
                else{
                    mClubProfileUpdate.file =  new File(Environment.getExternalStorageDirectory(),"temp_" + System.currentTimeMillis()/1000);
                }

                int id = getActivity().getIntent().getIntExtra("clubId", -1);
//                if(id == -1){
//                    getActivity().finish();
//                }

                mClubProfileUpdate.club_id= PropertyManager.getInstance().getMyPageResult().club_id;
                Log.d("hello", "id : " +PropertyManager.getInstance().getMyPageResult().club_id );
                Log.d("hello", "id : " +id );

                mClubProfileUpdate.clubLocationName =clubLocationName.getText().toString();
//                mClubProfileUpdate.latitude =91.0;
//                mClubProfileUpdate.longitude=92.0;

                if(clubMainDay_Mon.isChecked()){
                    mClubProfileUpdate.clubMainDay_Mon=1;
                }else{
                    mClubProfileUpdate.clubMainDay_Mon=0;
                }
                if(clubMainDay_Tue.isChecked()){
                    mClubProfileUpdate.clubMainDay_Tue=1;
                }else{
                    mClubProfileUpdate.clubMainDay_Tue=0;
                }
                if(clubMainDay_Wed.isChecked()){
                    mClubProfileUpdate.clubMainDay_Wed=1;
                }else{
                    mClubProfileUpdate.clubMainDay_Wed=0;
                }
                if(clubMainDay_Thu.isChecked()){
                    mClubProfileUpdate.clubMainDay_Thu=1;
                }else{
                    mClubProfileUpdate.clubMainDay_Thu=0;
                }
                if(clubMainDay_Fri.isChecked()){
                    mClubProfileUpdate.clubMainDay_Fri=1;
                }else{
                    mClubProfileUpdate.clubMainDay_Fri=0;
                }
                if(clubMainDay_Sat.isChecked()){
                    mClubProfileUpdate.clubMainDay_Sat=1;
                }else{
                    mClubProfileUpdate.clubMainDay_Sat=0;
                }
                if(clubMainDay_Sun.isChecked()){
                    mClubProfileUpdate.clubMainDay_Sun=1;
                }else{
                    mClubProfileUpdate.clubMainDay_Sun=0;
                }

                if(memYN.isChecked()){
                    mClubProfileUpdate.memYN =1;
                }else{
                    mClubProfileUpdate.memYN =0;
                }
                if(fieldYN.isChecked()){
                    mClubProfileUpdate.fieldYN =1;
                }else{
                    mClubProfileUpdate.fieldYN =0;
                }


                mClubProfileUpdate.clubField =clubField.getText().toString();

                if(matchYN.isChecked()){
                    mClubProfileUpdate.matchYN =1;
                }else{
                    mClubProfileUpdate.matchYN =0;
                }

                mClubProfileUpdate.clubIntro=clubIntro.getText().toString();

                NetworkManager.getInstance().postNetworkUpdateClub(getContext(), mClubProfileUpdate, new NetworkManager.OnResultListener<EtcData>() {
                    @Override
                    public void onSuccess(EtcData result) {
                        if(result.code==200){
                            getActivity().finish();

                        }else{
                            Toast.makeText(MyApplication.getContext(), result.msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFail(int code) {

                    }
                });



            }
        });


        clubImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.ic_launcher);
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
        //fc 없애기
        btn = (Button)view.findViewById(R.id.button33);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("FC없애기");
                builder.setMessage("FC를 해산하시겠습니까? ");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

//                        Fragment mFragment = (Fragment) MainFragment.newInstance("", "");
//                        getChildFragmentManager().beginTransaction()
//                                .replace(R.id.container, mFragment)
//                                .commit();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setCancelable(true);
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


        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(50))
                .build();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("hello", "onActivityResult before");
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("hello", "onActivityResult");
        if (requestCode == REQUEST_CODE_CROP && resultCode == Activity.RESULT_OK) {
            Bitmap bm = BitmapFactory.decodeFile(mSavedFile.getAbsolutePath());
            clubImage.setImageBitmap(bm);

            Log.d("hello", mSavedFile.getAbsolutePath().toString());
        }
        if(requestCode == REQ_AREA_SEARCH && resultCode == Activity.RESULT_OK){
            String userArea = data.getExtras().getString("userArea");
            double latitude = data.getExtras().getDouble("latitude");
            mClubProfileUpdate.latitude = latitude;
            double longitude = data.getExtras().getDouble("longitude");
            mClubProfileUpdate.longitude = longitude;
            clubLocationName.setText(userArea);
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

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            getActivity().setTitle("기본 설정");
//        }
//    }


    private void searchHeaderFCMember() {
        NetworkManager.getInstance().getNetworkClubMain(getContext(), clubId,new NetworkManager.OnResultListener<ClubMain>() {
            @Override
            public void onSuccess(ClubMain result) {
                for(ClubMainResult item : result.items){
                    setDefaultInfo(item);
                }
            }

            @Override
            public void onFail(int code) {
            }
        });

    }
    public void setDefaultInfo(ClubMainResult item){
        mItem = item;
        clubName.setText(item.clubName);
        clubIntro.setText(item.clubIntro);
        clubField.setText(item.clubField);

        clubLocationName.setText(item.clubLocationName);

        if(item.clubMainDay_Mon ==0){
            clubMainDay_Mon.setChecked(false);
        }else{
            clubMainDay_Mon.setChecked(true);
        }
        if(item.clubMainDay_Tue ==0){
            clubMainDay_Tue.setChecked(false);
        }else{
            clubMainDay_Tue.setChecked(true);
        }
        if(item.clubMainDay_Wed ==0){
            clubMainDay_Wed.setChecked(false);
        }else{
            clubMainDay_Wed.setChecked(true);
        }
        if(item.clubMainDay_Thu ==0){
            clubMainDay_Thu.setChecked(false);
        }else{
            clubMainDay_Thu.setChecked(true);
        }
        if(item.clubMainDay_Fri ==0){
            clubMainDay_Fri.setChecked(false);
        }else{
            clubMainDay_Fri.setChecked(true);
        }
        if(item.clubMainDay_Sat ==0){
            clubMainDay_Sat.setChecked(false);
        }else{
            clubMainDay_Sat.setChecked(true);
        }
        if(item.clubMainDay_Sun ==0){
            clubMainDay_Sun.setChecked(false);
        }else{
            clubMainDay_Sun.setChecked(true);
        }

        if(item.memYN ==0){
            memYN.setChecked(false);
        }else{
            memYN.setChecked(true);
        }
        if(item.matchYN ==0){
            matchYN.setChecked(false);
        }else{
            matchYN.setChecked(true);
        }

        if(item.fieldYN ==0){
            fieldYN.setChecked(false);
        }else{
            fieldYN.setChecked(true);
        }

        ImageLoader.getInstance().displayImage((ImageUrl + item.clubImage), clubImage, options);

    }
}
