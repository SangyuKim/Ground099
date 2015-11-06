package com.android.ground.ground.controller.fc.fcmain;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.management.FCManagementFragment;
import com.android.ground.ground.controller.person.message.CustomDialogMessageFragment;
import com.android.ground.ground.controller.person.profile.MyProfileFragment;
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.fc.fcmain.FCMemberListItem;
import com.android.ground.ground.model.naver.MovieAdapter;
import com.android.ground.ground.model.person.main.AlarmItemData;
import com.android.ground.ground.view.OnAdapterProfileListener;
import com.android.ground.ground.view.OnAdapterReplyListener;
import com.android.ground.ground.view.fc.fcmain.FCMemberHeaderItemView;
import com.android.ground.ground.view.fc.fcmain.FCMemberHeaderItemView2;
import com.android.ground.ground.view.fc.fcmain.FCMemberItemView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFCMember.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFCMember#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFCMember extends Fragment {

    ListView listView;
    FCMemberAdapter mAdapter;
    PullToRefreshListView refreshView;

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
        this.setUserVisibleHint(true);
        refreshView = (PullToRefreshListView)view.findViewById(R.id.view_fcmember);
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        listView = refreshView.getRefreshableView();
        mAdapter = new FCMemberAdapter();
        listView.addHeaderView(new FCMemberHeaderItemView(getContext()));
        listView.addHeaderView(new FCMemberHeaderItemView2(getContext()));
        Button btn = (Button)view.findViewById(R.id.button24);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mFragment = (Fragment) FCManagementFragment.newInstance("", "");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, mFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
        btn = (Button)view.findViewById(R.id.button22);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("가입신청");
                builder.setMessage("가입 신청하시겠습니까? ");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
        btn = (Button)view.findViewById(R.id.button23);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogMessageFragment dialog = new CustomDialogMessageFragment();
                dialog.show(getChildFragmentManager(), "custom");
            }
        });
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });

        initData();

        mAdapter.setOnAdapterReplyListener(new OnAdapterReplyListener() {
            @Override
            public void onAdapterReplyClick(Adapter adapter, View view) {
                CustomDialogMessageFragment dialog = new CustomDialogMessageFragment();
                dialog.show(getChildFragmentManager(), "custom");
            }
        });
        mAdapter.setOnAdapterProfileListener(new OnAdapterProfileListener() {
            @Override
            public void onAdapterProfileClick(Adapter adapter, View view, Profile data) {
                if(data instanceof FCFragment){
                    Fragment mFragment = (Fragment) FCFragment.newInstance("", "");
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, mFragment)
                            .addToBackStack(null)
                            .commit();
                }else if(data instanceof MyProfileFragment) {
                    Fragment mFragment = (Fragment) MyProfileFragment.newInstance("", "");
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, mFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return view;
    }
    public void initData(){
        for(int i=0; i< 20; i ++){
            FCMemberListItem data = new FCMemberListItem();

            mAdapter.add(data);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getActivity().setTitle("FC&멤버");
        }
    }

}
