package com.android.ground.ground.controller.person.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.fc.management.FCManagementActivity;
import com.android.ground.ground.controller.person.message.MyMessageFragment;
import com.android.ground.ground.controller.person.profile.YourProfileActivity;
import com.android.ground.ground.manager.ActivityManager;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.message.MyMessageData;
import com.android.ground.ground.model.message.MyMessageDataResult;
import com.android.ground.ground.model.noti.NotiData;
import com.android.ground.ground.model.noti.NotiDataResult;
import com.android.ground.ground.model.person.main.AlarmItemData;
import com.android.ground.ground.view.OnAlarmClickListener;
import com.android.ground.ground.view.person.main.AlarmItemView;
import com.android.ground.ground.view.person.message.MyMessageItemView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlarmFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlarmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmFragment extends Fragment {
    PullToRefreshListView refreshView;
    MainAlarmAdapter mAdapter;
    ListView listView;
    boolean isUpdate = false;

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
     * @return A new instance of fragment AlarmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AlarmFragment() {
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

        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        refreshView = (PullToRefreshListView)view.findViewById(R.id.listView_alarm);
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                searchNoti();
            }
        });

        refreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                getMoreItem();
            }
        });

        listView = refreshView.getRefreshableView();

        mAdapter = new MainAlarmAdapter();
        searchNoti();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAlarmListener.onDialogClick(false);
                NotiDataResult item = ((AlarmItemView) view).mNotiDataResult;
                if(item.sender != PropertyManager.getInstance().getUserId()) {
                    switch (((AlarmItemView) view).mNotiDataResult.code) {
                        case 100: {
                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            Fragment mFragment = (Fragment) MyMessageFragment.newInstance("", "");
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, mFragment)
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        }
                        case 200: {
                            //fc 메시지함으로 이동
                            Intent intent = new Intent(getContext(), FCManagementActivity.class);
                            intent.putExtra("clubId", PropertyManager.getInstance().getMyPageResult().club_id);
                            startActivity(intent);

                            break;
                        }
                        case 201: {
                            Intent intent = new Intent(getContext(), FCManagementActivity.class);
                            intent.putExtra("clubId", PropertyManager.getInstance().getMyPageResult().club_id);
                            startActivity(intent);

                            break;
                        }
                        case 300: {
                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            Fragment mFragment = (Fragment) MyMessageFragment.newInstance("", "");
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, mFragment)
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        }
                        case 301: {
                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            Fragment mFragment = (Fragment) MyMessageFragment.newInstance("", "");
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, mFragment)
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        }
                        case 302: {
                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            Fragment mFragment = (Fragment) MyMessageFragment.newInstance("", "");
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, mFragment)
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        }
                        case 400: {
                            Intent intent = new Intent(getContext(), FCManagementActivity.class);
                            intent.putExtra("clubId", PropertyManager.getInstance().getMyPageResult().club_id);
                            startActivity(intent);

                            break;
                        }
                        case 401: {
                            Intent intent = new Intent(getContext(), FCManagementActivity.class);
                            intent.putExtra("clubId", PropertyManager.getInstance().getMyPageResult().club_id);
                            startActivity(intent);

                            break;
                        }
                        case 500: {
                            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            Fragment mFragment = (Fragment) MyMessageFragment.newInstance("", "");
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container, mFragment)
                                    .addToBackStack(null)
                                    .commit();
                            break;
                        }

                    }
                }
            }
        });

        listView.setAdapter(mAdapter);

        return view;
    }

    private void searchNoti() {
        // PropertyManager.getInstance().getUserId()
        NetworkManager.getInstance().getNetworkNoti(getContext(),1, 1, new NetworkManager.OnResultListener<NotiData>() {
            @Override
            public void onSuccess(NotiData result) {
                for(NotiDataResult item : result.items){
                      mAdapter.add(item);
                }
            }

            @Override
            public void onFail(int code) {

            }
        });
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
    OnAlarmClickListener mAlarmListener;
    public void setOnAlarmClickListener(OnAlarmClickListener listener){
        mAlarmListener =listener;
    }
    private void getMoreItem() {
        if (!isUpdate) {
            int nextPage = mAdapter.getNextPage();
            if (nextPage != -1) {
                isUpdate = true;
                //PropertyManager.getInstance().getUserId()
                NetworkManager.getInstance().getNetworkNoti(getContext(), 1, nextPage, new NetworkManager.OnResultListener<NotiData>() {
                    @Override
                    public void onSuccess(NotiData result) {
                        for (NotiDataResult item : result.items) {
                            mAdapter.add(item);
                        }
                        isUpdate = false;
                    }

                    @Override
                    public void onFail(int code) {
                        isUpdate = false;
                    }
                });
            }
        }
    }

}
