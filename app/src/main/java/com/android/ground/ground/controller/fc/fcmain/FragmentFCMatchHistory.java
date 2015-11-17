package com.android.ground.ground.controller.fc.fcmain;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.fc.fcmain.ClubMatchList.ClubMatchList;
import com.android.ground.ground.model.fc.fcmain.ClubMatchList.ClubMatchListResult;
import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMain;
import com.android.ground.ground.model.fc.fcmain.clubMain.ClubMainResult;
import com.android.ground.ground.view.OnExpandableAdapterDialogListener;
import com.android.ground.ground.view.fc.fcmain.FCMatchHistoryHeaderItemView;
import com.android.ground.ground.view.fc.fcmain.FCMatchHistoryHeaderItemView2;
import com.android.ground.ground.view.fc.fcmain.FCMatchHistoryItemView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentFCMatchHistory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentFCMatchHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFCMatchHistory extends Fragment {
    int clubId;

    FCMatchHistoryHeaderItemView mFCMatchHistoryHeaderItemView;

    ExpandableListView listView;
    FCMatchHistoryAdapter mAdapter;
    PullToRefreshExpandableListView refreshView;
    List<ClubMatchListResult> totalChildren = new ArrayList<ClubMatchListResult>();

    List<ClubMatchListResult> readyChildren = new ArrayList<ClubMatchListResult>();
    List<ClubMatchListResult> ingChildren = new ArrayList<ClubMatchListResult>();
    List<ClubMatchListResult> finishedChildren = new ArrayList<ClubMatchListResult>();

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
     * @return A new instance of fragment FragmentFCMatchHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentFCMatchHistory newInstance(String param1, String param2) {
        FragmentFCMatchHistory fragment = new FragmentFCMatchHistory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentFCMatchHistory() {
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
        View view =inflater.inflate(R.layout.fragment_fragment_fcmatch_history, container, false);

        clubId = getActivity().getIntent().getIntExtra("clubId", -1);
        if(clubId == -1)
            clubId = PropertyManager.getInstance().getMyPageResult().club_id;

        refreshView = (PullToRefreshExpandableListView)view.findViewById(R.id.view_fcmatch_history);
//        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ExpandableListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
//                searchFCMatchList();
//            }
//        });

        listView = refreshView.getRefreshableView();
        mAdapter = new FCMatchHistoryAdapter();
        mFCMatchHistoryHeaderItemView = new FCMatchHistoryHeaderItemView(getContext());
        searchHeaderFCMatchList();
        listView.addHeaderView(mFCMatchHistoryHeaderItemView);
        listView.addHeaderView(new FCMatchHistoryHeaderItemView2(getContext()));
        listView.setAdapter(mAdapter);
        listView.setGroupIndicator(null);
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                listView.expandGroup(groupPosition);
            }
        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {
                    Intent intent = new Intent(getContext(), ReadyMatchResultActivity.class);
                    int matchId = ((FCMatchHistoryItemView)v).mItem.match_id;
                    intent.putExtra("matchId", matchId);
                    intent.putExtra("clubId", clubId);
                    intent.putExtra("groupPosition",groupPosition);
                    startActivity(intent);
                } else if (groupPosition == 1) {
                    Intent intent = new Intent(getContext(), ReadyMatchResultActivity.class);
                    int matchId = ((FCMatchHistoryItemView)v).mItem.match_id;
                    intent.putExtra("matchId", matchId);
                    intent.putExtra("clubId", clubId);
                    intent.putExtra("groupPosition",groupPosition);
                    startActivity(intent);
                } else if (groupPosition == 2) {
                    Intent intent = new Intent(getContext(), ReadyMatchResultActivity.class);
                    int matchId = ((FCMatchHistoryItemView)v).mItem.match_id;
                    intent.putExtra("matchId", matchId);
                    intent.putExtra("clubId", clubId);
                    intent.putExtra("groupPosition",groupPosition);
                    startActivity(intent);
                }

                return true;
            }
        });
        searchFCMatchList();


        mAdapter.setOnExpandableAdapterProfileListener(new OnExpandableAdapterDialogListener() {
            @Override
            public void onAdapterDialogClick(ExpandableListAdapter adapter, View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("경기 정정하기");
                builder.setMessage("정정 신청하시겠습니까? ");
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

        return view;
    }
    public void expandGroup(){
        for(int i=0; i <mAdapter.getGroupCount(); i++){
            listView.expandGroup(i);
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
//
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
//            getActivity().setTitle("경기 기록");
//        }
//    }

    private void searchFCMatchList() {

    NetworkManager.getInstance().getNetworkClubMatchList(getContext(), clubId, 1
                , new NetworkManager.OnResultListener<ClubMatchList>() {
            @Override
            public void onSuccess(ClubMatchList result) {
                int maxPage = result.maxPage;
                totalChildren.clear();
                readyChildren.clear();
                ingChildren.clear();
                finishedChildren.clear();

                mAdapter.setTotalCount(result.itemCount);
                mAdapter.setPgae(1);
                mAdapter.clear();
//                    for (ClubMatchListResult item : result.items) {
//                        mAdapter.add(item);
//                    }
                totalChildren = result.items;
                if(maxPage > 1){
                    for(int i=1; i<maxPage; i++){
                        NetworkManager.getInstance().getNetworkClubMatchList(getContext(),clubId, (i+1)
                                , new NetworkManager.OnResultListener<ClubMatchList>(){
                            @Override
                            public void onSuccess(ClubMatchList result) {
                                int oldPage = mAdapter.getPage();
                                 mAdapter.setPgae(oldPage++);
                                 totalChildren.addAll(result.items);
                            }

                            @Override
                            public void onFail(int code) {

                            }
                        });
                    }//for
                }//if
                divideTotalList(totalChildren);

                mAdapter.add("예정된 매치", readyChildren);
                mAdapter.add("결과 입력중 매치", ingChildren);
                mAdapter.add("마무리된 매치", finishedChildren);
                expandGroup();
            }

            @Override
            public void onFail(int code) {
            }
        });
    }

    private void divideTotalList(List<ClubMatchListResult> totalChildren) {
        for(ClubMatchListResult item : totalChildren){
            if(item.insertResultYN==4){
                readyChildren.add(item); ;
            }else if(item.insertResultYN==1){
                finishedChildren.add(item);
            }else{
                ingChildren.add(item);
            }

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());

    }

    private void searchHeaderFCMatchList() {
        NetworkManager.getInstance().getNetworkClubMain(getContext(),clubId,new NetworkManager.OnResultListener<ClubMain>() {
            @Override
            public void onSuccess(ClubMain result) {
                for(ClubMainResult item : result.items){
                    mFCMatchHistoryHeaderItemView.setFCMatchHeader(item);
                }
            }

            @Override
            public void onFail(int code) {
            }
        });
    }

}
