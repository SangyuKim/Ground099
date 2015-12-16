package com.android.ground.ground.controller.fc.management;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.person.message.CustomDialogMessageFragment;
import com.android.ground.ground.controller.person.profile.YourProfileActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.etc.EtcData;
import com.android.ground.ground.model.message.ClubMessageData;
import com.android.ground.ground.model.message.ClubMessageDataResult;
import com.android.ground.ground.model.post.push.MessageDeleteData;
import com.android.ground.ground.view.OnAdapterNoListener;
import com.android.ground.ground.view.OnAdapterProfileListener;
import com.android.ground.ground.view.OnAdapterReplyListener;
import com.android.ground.ground.view.OnAdapterYesListener;
import com.android.ground.ground.view.person.message.MyMessageItemViewEdit;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentClubMessageEdit.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentClubMessageEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentClubMessageEdit extends Fragment {
    View selectedView;
    boolean isUpdate = false;
    PullToRefreshListView refreshView;
    ListView listView;
    ClubMessageEditAdapter mAdapter;
//    List<MyMessageItem> items = new ArrayList<MyMessageItem>();
    Button btn, btn2;
    LinearLayout mLinearLayout;
    boolean isAllchecked= false;
    Handler mHandler;

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
     * @return A new instance of fragment FragmentClubMessageEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentClubMessageEdit newInstance(String param1, String param2) {
        FragmentClubMessageEdit fragment = new FragmentClubMessageEdit();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentClubMessageEdit() {
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
        View view = inflater.inflate(R.layout.fragment_fragment_club_message_edit, container, false);

        refreshView = (PullToRefreshListView)view.findViewById(R.id.listView_my_message);
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                for(int i=0; i< mAdapter.getCount()+1; i++){

                    listView.setItemChecked(i, false);
                    if(!mAdapter.getChecked(i))
                        mAdapter.setChecked(i);
                    // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                    mAdapter.notifyDataSetChanged();
                    isAllchecked = false;
                }
                searchClubMessage();
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
        mAdapter = new ClubMessageEditAdapter();
        mHandler = new Handler();
        searchClubMessage();
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageDeleteData mMessageDeleteData = new MessageDeleteData();
                selectedView = view;
                ClubMessageDataResult item = ((MyMessageItemViewEdit)view).mClubMessageDataResult;
                if(item != null){
                    mMessageDeleteData.message_id = item.message_id;
                    mMessageDeleteData.member_id = PropertyManager.getInstance().getUserId();
                    NetworkManager.getInstance().postNetworkMessageWatch(getContext(), mMessageDeleteData, new NetworkManager.OnResultListener<EtcData>() {
                        @Override
                        public void onSuccess(EtcData result) {
//                            selectedView.setBackgroundColor(getResources().getColor(R.color.gray));
                        }

                        @Override
                        public void onFail(int code) {

                        }
                    });
                }
            }
        });

        //전체선택
        btn2 = (Button)view.findViewById(R.id.button6);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAllchecked){

                    for(int i=0; i< mAdapter.getCount()+1; i++){

                        listView.setItemChecked(i, true);
                        if(!mAdapter.getChecked(i))
                            mAdapter.setChecked(i);
                        // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                        mAdapter.notifyDataSetChanged();
                        isAllchecked = true;
                    }
                }else{
                    for(int i=0; i< mAdapter.getCount()+1; i++){

                        listView.setItemChecked(i, false);
                        if(mAdapter.getChecked(i))
                            mAdapter.setChecked(i);
                        // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
                        mAdapter.notifyDataSetChanged();
                        isAllchecked = false;

                    }
                }
            }
        });

        ImageView btnClear = (ImageView)view.findViewById(R.id.button12);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChoiceItem();
            }
        });

//        mLinearLayout.setVisibility(View.VISIBLE);
        mAdapter.setCheckBoxVisible(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnAdapterProfileListener(new OnAdapterProfileListener() {
            @Override
            public void onAdapterProfileClick(Adapter adapter, View view) {

                ClubMessageDataResult item = ((MyMessageItemViewEdit) view).mClubMessageDataResult;
                if (item.sender != PropertyManager.getInstance().getUserId()) {
                    switch (((MyMessageItemViewEdit) view).mClubMessageDataResult.code) {
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
                ClubMessageDataResult item = ((MyMessageItemViewEdit) view).mClubMessageDataResult;
                if (item.sender != PropertyManager.getInstance().getMyPageResult().club_id) {
                    Toast.makeText(getContext(), "YES", Toast.LENGTH_SHORT).show();



                }

            }
        });
        mAdapter.setOnAdapterNoListener(new OnAdapterNoListener() {
            @Override
            public void onAdapterNoClick(Adapter adapter, View view) {
                ClubMessageDataResult item = ((MyMessageItemViewEdit) view).mClubMessageDataResult;
                if (item.sender != PropertyManager.getInstance().getMyPageResult().club_id) {
                    Toast.makeText(getContext(), "NO", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mAdapter.setOnAdapterReplyListener(new OnAdapterReplyListener() {
            @Override
            public void onAdapterReplyClick(Adapter adapter, View view) {
                ClubMessageDataResult item = ((MyMessageItemViewEdit) view).mClubMessageDataResult;
                 getActivity().getIntent().putExtra("collector_id", item.sender);
                if(item!=null){
                    if(item.sender != PropertyManager.getInstance().getUserId()) {
                        CustomDialogMessageFragment dialog = new CustomDialogMessageFragment();
                        dialog.show( (getActivity()).getSupportFragmentManager(), "custom");
                    }
                }

            }
        });


        return view;
    }
    private void onChoiceItem() {
        //삭제 포스트 날리기
        SparseBooleanArray selection = listView.getCheckedItemPositions();
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < selection.size(); index++) {
            int position = selection.keyAt(index);
            if (selection.get(position)) {
                sb.append(Integer.toString(position)).append(",");
                MessageDeleteData mMessageDeleteData = new MessageDeleteData();

                View view = getViewByPosition(position, listView);

                mMessageDeleteData.member_id = PropertyManager.getInstance().getUserId();
                if(view instanceof MyMessageItemViewEdit )
                  mMessageDeleteData.message_id =((MyMessageItemViewEdit)view).mClubMessageDataResult.message_id;
                NetworkManager.getInstance().postNetworkMessageDelete(getContext(), mMessageDeleteData, new NetworkManager.OnResultListener<EtcData>() {
                    @Override
                    public void onSuccess(EtcData result) {
                                 }

                    @Override
                    public void onFail(int code) {
                           }
                });
            }
        }
//        Toast.makeText(getContext(), "items : " + sb.toString(), Toast.LENGTH_SHORT).show();
        for(int i=0; i< mAdapter.getCount()+1; i++){

            listView.setItemChecked(i, false);
            if(!mAdapter.getChecked(i))
                mAdapter.setChecked(i);
            // Data 변경시 호출 Adapter에 Data 변경 사실을 알려줘서 Update 함.
            mAdapter.notifyDataSetChanged();
            isAllchecked = false;
        }
        searchClubMessage();

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

    public void searchClubMessage() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                NetworkManager.getInstance().getNetworkClubMessage(getContext(), PropertyManager.getInstance().getMyPageResult().club_id, 1, new NetworkManager.OnResultListener<ClubMessageData>() {
                    @Override
                    public void onSuccess(ClubMessageData result) {
                        mAdapter.setTotalCount(result.itemCount);
                        mAdapter.setPgae(1);
                        mAdapter.clear();
                        for (ClubMessageDataResult item : result.items) {
                            mAdapter.add(item);
                        }
//                mAdapter = new MyMessageAdapter(getContext(), result.items);
                        refreshView.onRefreshComplete();
                    }

                    @Override
                    public void onFail(int code) {
                    }
                });
            }
        });

    }
    private void getMoreItem() {
        if (!isUpdate) {
            int nextPage = mAdapter.getNextPage();
            if (nextPage != -1) {
                isUpdate = true;
                NetworkManager.getInstance().getNetworkClubMessage(getContext(), PropertyManager.getInstance().getMyPageResult().club_id, nextPage, new NetworkManager.OnResultListener<ClubMessageData>() {
                    @Override
                    public void onSuccess(ClubMessageData result) {
                        for (ClubMessageDataResult item : result.items) {
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
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;


        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            try{
            return listView.getAdapter().getView(pos, null, listView);
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        } else {
            try{
                final int childIndex = pos - firstListItemPosition;
                return listView.getChildAt(childIndex);
            }catch(IndexOutOfBoundsException e){
                e.printStackTrace();
            }

        }
        return null;


    }

}
