package com.android.ground.ground.controller.person.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.person.profile.MyPage;
import com.android.ground.ground.model.person.profile.MyPageResult;
import com.android.ground.ground.model.person.profile.MyPageTrans;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    TextView tabHostTextView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TabHost tabHost;
    ViewPager pager;
    TabsAdapter mAdapter;
    TabWidget tabWidget;

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
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
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
        View view  =inflater.inflate(R.layout.fragment_main, container, false);

        tabHost = (TabHost)view.findViewById(R.id.tabHost);
//        tabHost.getTabWidget().setLayoutParams();
        tabHost.setup();

        pager = (ViewPager)view.findViewById(R.id.pager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                MyApplication.getmIMM().hideSoftInputFromWindow(pager.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

         mAdapter = new TabsAdapter(getContext(), getChildFragmentManager(), tabHost, pager);

        View tabView = createTabView(tabHost.getContext(), R.drawable.tab_101);
        mAdapter.addTab(tabHost.newTabSpec("tab1").setIndicator(tabView), FragmentMainSearchPlayer.class, null);
        tabView = createTabView(tabHost.getContext(), R.drawable.tab_102);
        mAdapter.addTab(tabHost.newTabSpec("tab2").setIndicator(tabView), FragmentMainSearchFC.class, null);
        tabView = createTabView(tabHost.getContext(), R.drawable.tab_103);
        mAdapter.addTab(tabHost.newTabSpec("tab3").setIndicator(tabView), FragmentMainCheckMatch.class, null);

        tabHost.setCurrentTab(1);

        if(tabHost.getCurrentTab()==1){
            for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(i).findViewById(R.id.imageView5);
                String tabID = "tab_10"+(i+1);
                int resID = getResources().getIdentifier(tabID, "drawable", "com.android.ground.ground");
                tabHostImageView.setImageResource(resID);
            }
            getActivity().setTitle("FC찾기");
            ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(1).findViewById(R.id.imageView5);
            tabHostImageView.setImageResource(R.drawable.tab_002);

        }
        mAdapter.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
//                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.BLACK); // unselected

                    ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(i).findViewById(R.id.imageView5);
                    String tabID = "tab_10"+(i+1);
                    int resID = getResources().getIdentifier(tabID, "drawable", "com.android.ground.ground");
                    tabHostImageView.setImageResource(resID);

                }
                if (tabId.equals("tab1")) {
                    getActivity().setTitle("선수찾기");
                    ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(0).findViewById(R.id.imageView5);
                    tabHostImageView.setImageResource(R.drawable.tab_001);

                } else if (tabId.equals("tab2")) {
                    getActivity().setTitle("FC찾기");
                    ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(1).findViewById(R.id.imageView5);
                    tabHostImageView.setImageResource(R.drawable.tab_002);

                } else if (tabId.equals("tab3")) {
                    getActivity().setTitle("매치확인");
                    ImageView tabHostImageView = (ImageView)tabHost.getTabWidget().getChildAt(2).findViewById(R.id.imageView5);
                    tabHostImageView.setImageResource(R.drawable.tab_003);

                }

            }
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());
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
    private static View createTabView(final Context context, final int res) {
        View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
//        TextView tv = (TextView) view.findViewById(R.id.tabsText);
//        tv.setText(text);
        ImageView imageViewTab = (ImageView)view.findViewById(R.id.imageView5);
//        imageViewTab.setImageResource();
        return view;
    }

    @Override
    public void onResume() {
        searchMyPage(PropertyManager.getInstance().getUserId());
        super.onResume();
    }
    private void searchMyPage(final int memberId) {
//        showWaitingDialog();
        NetworkManager.getInstance().getNetworkMyPage(getContext(), memberId, new NetworkManager.OnResultListener<MyPage>() {
            @Override
            public void onSuccess(MyPage result) {
//                unShowWaitingDialog();

                for (MyPageResult item : result.items) {
                    PropertyManager.getInstance().setMyPageResult(item);
                    searchMyPageTrans(memberId);
                }
            }

            @Override
            public void onFail(int code) {
//                Toast.makeText(SplashActivity.this, "선수 정보 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
//                unShowWaitingDialog();
            }
        });
    }
    private void searchMyPageTrans(final int memberId) {

//        showWaitingDialog();
        NetworkManager.getInstance().getNetworkMyPageTrans(getContext(), memberId, new NetworkManager.OnResultListener<MyPageTrans>() {
            @Override
            public void onSuccess(MyPageTrans result) {
//                unShowWaitingDialog();
                Log.d("hello", PropertyManager.getInstance().getDeviceId());
                Log.d("hello", PropertyManager.getInstance().getRegistrationToken());

                PropertyManager.getInstance().setMyPageTransResult(result.items);

            }

            @Override
            public void onFail(int code) {
//                Toast.makeText(SplashActivity.this, "선수 정보 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
//                unShowWaitingDialog();
            }
        });
    }
}
