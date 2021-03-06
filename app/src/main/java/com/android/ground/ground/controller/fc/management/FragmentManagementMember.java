package com.android.ground.ground.controller.fc.management;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.message.CustomDialogMessageFragment;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.etc.EtcData;
import com.android.ground.ground.model.fc.fcmain.ClubAndMember.ClubAndMember;
import com.android.ground.ground.model.fc.fcmain.ClubAndMember.ClubAndMemberResult;
import com.android.ground.ground.model.post.ClubManagerData;
import com.android.ground.ground.model.post.push.Push301Response;
import com.android.ground.ground.view.fc.management.ManagementMemberItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentManagementMember.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentManagementMember#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentManagementMember extends Fragment {

    int clubId;

    ListView listView;
    ManagemnetMemberAdapter mAdapter;
    List<ClubAndMemberResult> items = new ArrayList<ClubAndMemberResult>();
    ArrayList<Integer> messageIds = new ArrayList<Integer>();
    Button btn;
    CheckBox btnEntire;
    boolean isAllchecked= false;
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
     * @return A new instance of fragment FragmentManagementMember.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentManagementMember newInstance(String param1, String param2) {
        FragmentManagementMember fragment = new FragmentManagementMember();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentManagementMember() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_management_member, container, false);

        clubId = PropertyManager.getInstance().getMyPageResult().club_id;

        listView = (ListView)view.findViewById(R.id.listView_management_member);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        searchClubAndMember();

        listView.setOnItemClickListener(mItemClickListener);
        btnEntire = (CheckBox)view.findViewById(R.id.button13);
        btnEntire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAllchecked) {

                    for (int i = 0; i < mAdapter.getCount(); i++) {

                        listView.setItemChecked(i, true);
                        if (!mAdapter.getChecked(i))
                            mAdapter.setChecked(i);
                        // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                        mAdapter.notifyDataSetChanged();
                        isAllchecked = true;
                    }
                } else {
                    for (int i = 0; i < mAdapter.getCount(); i++) {

                        listView.setItemChecked(i, false);
                        if (mAdapter.getChecked(i))
                            mAdapter.setChecked(i);
                        // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                        mAdapter.notifyDataSetChanged();
                        isAllchecked = false;

                    }
                }
            }
        });
        //임명
        ImageView btn3 = (ImageView)view.findViewById(R.id.button38);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChoiceItem(0);
            }
        });
        //박탈
        btn3 = (ImageView)view.findViewById(R.id.button39);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChoiceItem(1);
            }
        });
        //메시지 보내기기
        btn3 = (ImageView)view.findViewById(R.id.button37);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChoiceItem(3);

            }
        });
        //추방
        btn3 = (ImageView)view.findViewById(R.id.button42);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChoiceItem(2);
            }
        });

        return view;
    }
    ClubManagerData mClubManagerData;
    private void onChoiceItem(final int AddStop) {
        SparseBooleanArray selection = listView.getCheckedItemPositions();
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < selection.size(); index++) {
            int position = selection.keyAt(index);
            if (selection.get(position)) {
                sb.append(Integer.toString(position)).append(",");
                mClubManagerData = new ClubManagerData();
                mClubManagerData.member_id = PropertyManager.getInstance().getUserId();
                mClubManagerData.club_id = PropertyManager.getInstance().getMyPageResult().club_id;
                View view = getViewByPosition(position, listView);
                mClubManagerData.manager_id =((ManagementMemberItemView)view).mItem.member_id;
                mClubManagerData.drop_id =((ManagementMemberItemView)view).mItem.member_id;
                messageIds.add(((ManagementMemberItemView)view).mItem.member_id);
                if(AddStop == 0 ){
                    NetworkManager.getInstance().postNetworkClubManager(getContext(), mClubManagerData, new NetworkManager.OnResultListener<EtcData>() {
                        @Override
                        public void onSuccess(EtcData result) {
                            if(result.code ==200){
                                Toast.makeText(getContext(), "매니저 임명 성공하였습니다. ", Toast.LENGTH_SHORT).show();
                            }else {
                                if(result.msg != null){
                                    try{
                                        Toast.makeText(getContext(),result.msg , Toast.LENGTH_SHORT).show();
                                    }catch(NullPointerException e){
                                        e.printStackTrace();
                                    }

                                }
                            }

                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(getContext(), "매니저 임명 실패하였습니다. ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else if(AddStop ==1){
                    NetworkManager.getInstance().postNetworkClubManagerStop(getContext(), mClubManagerData, new NetworkManager.OnResultListener<EtcData>() {
                        @Override
                        public void onSuccess(EtcData result) {
                            if(result.code ==200){
                                Toast.makeText(getContext(), "매니저 박탈 성공하였습니다. ", Toast.LENGTH_SHORT).show();
                            }else {
                                if(result.msg != null){
                                    try{
                                        Toast.makeText(getContext(),result.msg , Toast.LENGTH_SHORT).show();
                                    }catch(NullPointerException e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(getContext(), "매니저 박탈 실패하였습니다. ", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else if(AddStop ==2){
                    NetworkManager.getInstance().postNetworkClubMemberDrop(getContext(), mClubManagerData, new NetworkManager.OnResultListener<EtcData>() {
                        @Override
                        public void onSuccess(EtcData result) {
                            if(result.code ==200){

                                final Push301Response mPush301Response = new Push301Response();
                                mPush301Response.member_id = PropertyManager.getInstance().getUserId();
                                mPush301Response.sender_id = PropertyManager.getInstance().getUserId();
                                mPush301Response.collector_id = mClubManagerData.drop_id;
                                NetworkManager.getInstance().postNetworkMessage306(getContext(), mPush301Response, new NetworkManager.OnResultListener<EtcData>() {
                                    @Override
                                    public void onSuccess(EtcData result) {
                                        NetworkManager.getInstance().postNetworkPush306(getContext(), mPush301Response, new NetworkManager.OnResultListener<EtcData>() {
                                            @Override
                                            public void onSuccess(EtcData result) {
                                                Toast.makeText(getContext(), "선수 추방 성공하였습니다. ", Toast.LENGTH_SHORT).show();
                                            }
                                            @Override
                                            public void onFail(int code) {
                                                Toast.makeText(getContext(), "선수 추방 푸시 실패하였습니다. ", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFail(int code) {
                                        Toast.makeText(getContext(), "선수 추방  메시지 실패하였습니다. ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                if(result.msg != null){
                                    try{
                                        Toast.makeText(getContext(),result.msg , Toast.LENGTH_SHORT).show();
                                    }catch(NullPointerException e){
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(getContext(), "선수 추방 실패하였습니다. ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }//for
        if(AddStop ==3){
            if(messageIds.size()>0){
                Log.d("hello", ""+ messageIds.get(0));
                getActivity().getIntent().putExtra("collector_ids", messageIds);
                CustomDialogMessageFragment dialog = new CustomDialogMessageFragment();
                dialog.show(getChildFragmentManager(), "custom");
            }


        }
//        Toast.makeText(getContext(), "items : " + sb.toString(), Toast.LENGTH_SHORT).show();



    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
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
    private AdapterView.OnItemClickListener mItemClickListener = new
            AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    mAdapter.setChecked(position);
                    // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                    mAdapter.notifyDataSetChanged();

                }
            };
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            getActivity().setTitle("멤버 관리");
//        }
//    }

    private void searchClubAndMember() {

        NetworkManager.getInstance().getNetworkClubAndMember(getContext(),clubId, new NetworkManager.OnResultListener<ClubAndMember>() {
            @Override
            public void onSuccess(ClubAndMember result) {
                for (ClubAndMemberResult item : result.items) {
                    items.add(item);
                }
                mAdapter = new ManagemnetMemberAdapter(getContext(), items);
                listView.setAdapter(mAdapter);

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
