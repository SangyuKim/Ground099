package com.android.ground.ground.controller.person.message;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.message.MyMessageData;
import com.android.ground.ground.model.message.MyMessageDataResult;
import com.android.ground.ground.model.person.message.MyMessageItem;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyMessageEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyMessageEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMessageEditFragment extends Fragment {
    boolean isUpdate = false;
    PullToRefreshListView refreshView;
    ListView listView;
    MyMessageEditAdapter mAdapter;
    List<MyMessageItem> items = new ArrayList<MyMessageItem>();
    Button btn, btn2;
    LinearLayout mLinearLayout;
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
     * @return A new instance of fragment MyMessageEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMessageEditFragment newInstance(String param1, String param2) {
        MyMessageEditFragment fragment = new MyMessageEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyMessageEditFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_message_edit, container, false);
        getActivity().setTitle("메시지 편집");


        refreshView = (PullToRefreshListView)view.findViewById(R.id.listView_my_message);
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                searchMyMessage();
            }
        });

        refreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                getMoreItem();
            }
        });
        listView = refreshView.getRefreshableView();
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        mAdapter = new MyMessageEditAdapter();
        searchMyMessage();

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(mItemClickListener);

        //전체선택
        //포지션 계산할때 +1 해서 계산하기
        //
        btn2 = (Button)view.findViewById(R.id.button6);
        mLinearLayout = (LinearLayout)view.findViewById(R.id.linearLayout_clear_cancel);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAllchecked) {
                    for (int i = 0; i < mAdapter.getCount()+1; i++) {
                        listView.setItemChecked(i, true);
                        if (!mAdapter.getChecked(i))
                            mAdapter.setChecked(i);
                        // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                        mAdapter.notifyDataSetChanged();
                        isAllchecked = true;
                    }
                } else {
                    for (int i = 0; i < mAdapter.getCount()+1; i++) {

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
        btn2.setVisibility(View.VISIBLE);
        mLinearLayout.setVisibility(View.VISIBLE);
        mAdapter.setCheckBoxVisible(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
        //편집 버튼
        btn  = (Button)view.findViewById(R.id.button11);
        btn.setText("편집취소");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        //삭제
        Button btn3 = (Button)view.findViewById(R.id.button12);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChoiceItem();
            }
        });

        return view;
    }

    private void onChoiceItem() {
        SparseBooleanArray selection = listView.getCheckedItemPositions();
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < selection.size(); index++) {
            int position = selection.keyAt(index);
            if (selection.get(position)) {
                sb.append(Integer.toString(position)).append(",");
            }
        }
        Toast.makeText(getContext(), "items : " + sb.toString(), Toast.LENGTH_SHORT).show();

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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getActivity().setTitle("메시지 편집");
        }
    }

    private void searchMyMessage() {

        NetworkManager.getInstance().getNetworkMessage(getContext(), PropertyManager.getInstance().getUserId(), 1, new NetworkManager.OnResultListener<MyMessageData>() {
            @Override
            public void onSuccess(MyMessageData result) {
                mAdapter.setTotalCount(result.itemCount);
                mAdapter.setPgae(1);
                mAdapter.clear();
                for (MyMessageDataResult item : result.items) {

                    mAdapter.add(item);
                }
//                mAdapter = new MyMessageAdapter(getContext(), result.items);
//                refreshView.onRefreshComplete();
            }

            @Override
            public void onFail(int code) {
            }
        });
    }
    private void getMoreItem() {
        if (!isUpdate) {
            int nextPage = mAdapter.getNextPage();
            if (nextPage != -1) {
                isUpdate = true;
                NetworkManager.getInstance().getNetworkMessage(getContext(), PropertyManager.getInstance().getUserId(), nextPage, new NetworkManager.OnResultListener<MyMessageData>() {
                    @Override
                    public void onSuccess(MyMessageData result) {
                        for (MyMessageDataResult item : result.items) {
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
