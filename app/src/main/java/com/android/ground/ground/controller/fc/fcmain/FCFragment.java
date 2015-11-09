package com.android.ground.ground.controller.fc.fcmain;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.main.FragmentMainCheckMatch;
import com.android.ground.ground.controller.person.main.FragmentMainSearchFC;
import com.android.ground.ground.controller.person.main.FragmentMainSearchPlayer;
import com.android.ground.ground.controller.person.main.TabsAdapter;
import com.android.ground.ground.model.Profile;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FCFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FCFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FCFragment extends Fragment implements Profile {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    TabHost tabHost;
    ViewPager pager;
    FCTabsAdapter mAdapter;

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
     * @return A new instance of fragment FCFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FCFragment newInstance(String param1, String param2) {
        FCFragment fragment = new FCFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FCFragment() {
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fc, container, false);

        tabHost = (TabHost)view.findViewById(R.id.tabHost);
        tabHost.setup();
        pager = (ViewPager)view.findViewById(R.id.pager);

        mAdapter = new FCTabsAdapter(getContext(), getChildFragmentManager(), tabHost, pager);

        mAdapter.addTab(tabHost.newTabSpec("tab1").setIndicator("FC & 멤버"), FragmentFCMember.class, null);
        mAdapter.addTab(tabHost.newTabSpec("tab2").setIndicator("경기 기록"), FragmentFCMatchHistory.class, null);

        if(tabHost.getCurrentTab()==0){
            getActivity().setTitle("FC & 멤버");
        }
        mAdapter.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                                             @Override
                                             public void onTabChanged(String tabId) {
                 if (tabId.equals("tab1")) {
                     getActivity().setTitle("FC & 멤버");
                 } else if (tabId.equals("tab2")) {
                     getActivity().setTitle("경기 기록");
                 }
                 }
         }
        );

        if (savedInstanceState != null) {
            tabHost.setCurrentTab(savedInstanceState.getInt("tabIndex"));
            mAdapter.onRestoreInstanceState(savedInstanceState);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tabIndex", tabHost.getCurrentTab());
        mAdapter.onSaveInstanceState(outState);
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
    public void onResume() {
        super.onResume();
    }
}
