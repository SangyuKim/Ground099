package com.android.ground.ground.controller.person.message;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.person.profile.MyProfileActivity;
import com.android.ground.ground.controller.person.profile.YourProfileActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.message.MyMessageData;
import com.android.ground.ground.model.message.MyMessageDataResult;
import com.android.ground.ground.model.person.main.searchMem.SearchMem;
import com.android.ground.ground.model.person.main.searchMem.SearchMemResult;
import com.android.ground.ground.model.person.message.MyMessageItem;
import com.android.ground.ground.view.OnAdapterNoListener;
import com.android.ground.ground.view.OnAdapterProfileListener;
import com.android.ground.ground.view.OnAdapterReplyListener;
import com.android.ground.ground.view.OnAdapterYesListener;
import com.android.ground.ground.view.person.main.SearchFCItemView;
import com.android.ground.ground.view.person.message.MyMessageItemView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyMessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMessageFragment extends Fragment {

    PullToRefreshListView refreshView;
    ListView listView;
    MyMessageAdapter mAdapter;
    List<MyMessageItem> items = new ArrayList<MyMessageItem>();
    Button btn;
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
     * @return A new instance of fragment MyMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMessageFragment newInstance(String param1, String param2) {
        MyMessageFragment fragment = new MyMessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyMessageFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_message, container, false);
        this.setUserVisibleHint(true);


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
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mAdapter = new MyMessageAdapter();
        searchMyMessage();

        listView.setAdapter(mAdapter);
       //편집
        btn  = (Button)view.findViewById(R.id.button11);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mFragment = (Fragment) MyMessageEditFragment.newInstance("", "");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, mFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        mAdapter.setOnAdapterProfileListener(new OnAdapterProfileListener() {
            @Override
            public void onAdapterProfileClick(Adapter adapter, View view) {

                MyMessageDataResult item = ((MyMessageItemView) view).mItem;
                if(item.sender != PropertyManager.getInstance().getUserId()) {
                    switch (((MyMessageItemView) view).mItem.code) {
                        case 100: {
                            //your profile 이동
                            Intent intent = new Intent(getContext(), YourProfileActivity.class);
                            intent.putExtra("memberId", item.sender);
                            startActivity(intent);

                            break;
                        }
                        case 200: {
                            //your profile 이동
                            Intent intent = new Intent(getContext(), YourProfileActivity.class);
                            intent.putExtra("memberId", item.sender);
                            startActivity(intent);

                            break;
                        }
                        case 201: {
                            //your profile 이동
                            Intent intent = new Intent(getContext(), YourProfileActivity.class);
                            intent.putExtra("memberId", item.sender);
                            startActivity(intent);

                            break;
                        }
                        case 300: {

                            Intent intent = new Intent(getContext(), FCActivity.class);
                            intent.putExtra("clubId", item.senderClub);
                            startActivity(intent);
                            break;
                        }
                        case 301: {
                            Intent intent = new Intent(getContext(), FCActivity.class);
                            intent.putExtra("clubId", item.senderClub);
                            startActivity(intent);
                            break;
                        }
                        case 302: {
                            Intent intent = new Intent(getContext(), FCActivity.class);
                            intent.putExtra("clubId", item.senderClub);
                            startActivity(intent);
                            break;
                        }
                        case 400: {
                            Intent intent = new Intent(getContext(), FCActivity.class);
                            intent.putExtra("clubId", item.senderClub);
                            startActivity(intent);
                            break;
                        }
                        case 401: {
                            Intent intent = new Intent(getContext(), FCActivity.class);
                            intent.putExtra("clubId", item.senderClub);
                            startActivity(intent);
                            break;
                        }
                        case 500: {
                            break;
                        }

                    }
                }
            }
        });

        mAdapter.setOnAdapterYesListener(new OnAdapterYesListener() {
            @Override
            public void onAdapterYesClick(Adapter adapter, View view) {
                MyMessageDataResult item = ((MyMessageItemView) view).mItem;
                if(item.sender != PropertyManager.getInstance().getUserId()) {
                    Toast.makeText(getContext(), "YES", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mAdapter.setOnAdapterNoListener(new OnAdapterNoListener() {
            @Override
            public void onAdapterNoClick(Adapter adapter, View view) {
                MyMessageDataResult item = ((MyMessageItemView) view).mItem;
                if(item.sender != PropertyManager.getInstance().getUserId()) {
                    Toast.makeText(getContext(), "NO", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mAdapter.setOnAdapterReplyListener(new OnAdapterReplyListener() {
            @Override
            public void onAdapterReplyClick(Adapter adapter, View view) {
                MyMessageDataResult item = ((MyMessageItemView) view).mItem;
                if(item.sender != PropertyManager.getInstance().getUserId()) {
                    CustomDialogMessageFragment dialog = new CustomDialogMessageFragment();
                    dialog.show(getChildFragmentManager(), "custom");
                }

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
//    private AdapterView.OnItemClickListener mItemClickListener = new
//            AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> arg0, View arg1,
//                                        int position, long arg3) {
//                    mAdapter.setChecked(position);
//                    // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
//                    mAdapter.notifyDataSetChanged();
//
//                }
//    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getActivity().setTitle("메시지함");
        }
    }
    private void searchMyMessage() {

        NetworkManager.getInstance().getNetworkMessage(getContext(), 1, 1, new NetworkManager.OnResultListener<MyMessageData>() {
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
                NetworkManager.getInstance().getNetworkMessage(getContext(), 1, nextPage, new NetworkManager.OnResultListener<MyMessageData>() {
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
