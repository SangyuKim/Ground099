package com.android.ground.ground.controller.fc.fcmain;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.management.FCManagementActivity;
import com.android.ground.ground.controller.person.message.CustomDialogMessageFragment;
import com.android.ground.ground.controller.person.profile.MyProfileActivity;
import com.android.ground.ground.controller.person.profile.YourProfileActivity;
import com.android.ground.ground.manager.ActivityManager;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.fc.fcmain.ClubAndMember.ClubAndMember;
import com.android.ground.ground.model.fc.fcmain.ClubAndMember.ClubAndMemberResult;
import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMain;
import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMainResult;
import com.android.ground.ground.view.OnAdapterProfileListener;
import com.android.ground.ground.view.OnAdapterReplyListener;
import com.android.ground.ground.view.fc.fcmain.FCMemberHeaderItemView;
import com.android.ground.ground.view.fc.fcmain.FCMemberHeaderItemView2;
import com.android.ground.ground.view.fc.fcmain.FCMemberItemView;
import com.android.ground.ground.view.person.main.SearchPlayerItemView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFCMember.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFCMember#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFCMember extends Fragment {
    FCMemberHeaderItemView mFCMemberHeaderItemView;
    ListView listView;
    FCMemberAdapter mAdapter;
    int clubId;

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
     * @return A new instance of fragment FragmentFCMember.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFCMember newInstance(String param1, String param2) {
        FragmentFCMember fragment = new FragmentFCMember();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentFCMember() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_fcmember, container, false);
//        this.setUserVisibleHint(true);
        clubId = getActivity().getIntent().getIntExtra("clubId", -1);
        if(clubId == -1)
            clubId = PropertyManager.getInstance().getMyPageResult().club_id;

        listView =  (ListView)view.findViewById(R.id.view_fcmember);
        mAdapter = new FCMemberAdapter();
        mFCMemberHeaderItemView = new FCMemberHeaderItemView(getContext());
        searchHeaderFCMember();
        listView.addHeaderView(mFCMemberHeaderItemView);
        listView.addHeaderView(new FCMemberHeaderItemView2(getContext()));

        Button btn = (Button)view.findViewById(R.id.button24);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment mFragment = (Fragment) FCManagementFragment.newInstance("", "");
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, mFragment)
//                        .addToBackStack(null)
//                        .commit();
                ActivityManager.getInstance().activityArrayList.add(getActivity());
                Intent intent = new Intent(getContext(), FCManagementActivity.class);
                intent.putExtra("clubId", clubId);
                startActivity(intent);

            }
        });

        btn = (Button)view.findViewById(R.id.button23);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogMessageFragment dialog = new CustomDialogMessageFragment();
                dialog.show(getChildFragmentManager(), "custom");
            }
        });


        listView.setAdapter(mAdapter);
        searchClubAndMember();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });


        mAdapter.setOnAdapterReplyListener(new OnAdapterReplyListener() {
            @Override
            public void onAdapterReplyClick(Adapter adapter, View view) {
                CustomDialogMessageFragment dialog = new CustomDialogMessageFragment();
                dialog.show(getChildFragmentManager(), "custom");
            }
        });
        mAdapter.setOnAdapterProfileListener(new OnAdapterProfileListener() {
            @Override
            public void onAdapterProfileClick(Adapter adapter, View view) {
//                if(data instanceof FCActivity){
//                    Intent intent = new Intent(getContext(), FCActivity.class);
//                    startActivity(intent);
//                }else if(data instanceof MyProfileActivity) {
//                    int memberId = ((FCMemberItemView)view).mItem.member_id;
//                    if(memberId == PropertyManager.getInstance().getMyPageResult().member_id){
//                        ActivityManager.getInstance().activityArrayList.add(getActivity());
//                        Intent intent = new Intent(getContext(), MyProfileActivity.class);
//                        startActivity(intent);
//                    }else{
////                        List<Activity> mList = ActivityManager.getInstance().activityArrayList;
////                        for(int i = 0; i < mList.size(); i++)
////                            mList.get(i).finish();
////
//                        ActivityManager.getInstance().activityArrayList.add(getActivity());
//                        Intent intent = new Intent(getContext(), YourProfileActivity.class);
//                        intent.putExtra("memberId",memberId);
//                        startActivity(intent);
//                    }
//                }
            }
        });

        return view;
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
        void onFragmentInteraction(Uri uri);
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            getActivity().setTitle("FC&멤버");
//        }
//    }


    private void searchHeaderFCMember() {
        NetworkManager.getInstance().getNetworkClubMain(getContext(), clubId,new NetworkManager.OnResultListener<ClubMain>() {
            @Override
            public void onSuccess(ClubMain result) {
                for(ClubMainResult item : result.items){
                     mFCMemberHeaderItemView.setFCMemberHeader(item);
                }
            }

            @Override
            public void onFail(int code) {
            }
        });

    }
    private void searchClubAndMember() {

            NetworkManager.getInstance().getNetworkClubAndMember(getContext(),clubId, new NetworkManager.OnResultListener<ClubAndMember>() {
                @Override
                public void onSuccess(ClubAndMember result) {
                    mAdapter.clear();
                    for (ClubAndMemberResult item : result.items) {
                        mAdapter.add(item);
                    }

                }
                @Override
                public void onFail(int code) {
                }
            });
        }


    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());
    }
}
