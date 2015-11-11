package com.android.ground.ground.controller.person.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.fcmain.FCActivity;
import com.android.ground.ground.controller.person.finalposition.FinalPositionActivity;
import com.android.ground.ground.controller.person.profile.MyProfileActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.person.main.matchinfo.MVP.MVP;
import com.android.ground.ground.model.person.main.matchinfo.MatchInfo;
import com.android.ground.ground.model.person.main.matchinfo.MatchInfoAdapter;
import com.android.ground.ground.model.person.main.searchClub.SearchClub;
import com.android.ground.ground.model.person.main.searchClub.SearchClubResult;
import com.android.ground.ground.view.person.main.MVPview;
import com.android.ground.ground.view.person.main.SearchMatchGroupItemView;
import com.android.ground.ground.view.person.main.SearchMatchItemView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMainCheckMatch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMainCheckMatch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMainCheckMatch extends Fragment implements MVPview.OnHeaderImageClickListener {

    //todo
    String filter;
    MVPview hView;

    boolean isMoreList = true;
    boolean isOpened = false;

    boolean flag1, flag2;

    View view;
    FloatingActionButton fab;
    Spinner spinner;
    MySpinnerAdapter mySpinnerAdapter;
    /*네이버 예제*/
    EditText keywordView;
    ExpandableListView listView;
    LinearLayout mLinearLayout;
    PullToRefreshExpandableListView refreshView;
    //    SwipeRefreshLayout refreshLayout;
    MatchInfoAdapter mAdapter;
    boolean isUpdate = false;
    boolean isMoreFutureItem = true;
    boolean isMoreIngItem = true;
    boolean isMoreEndItem = true;

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
     * @return A new instance of fragment FragmentMainCheckMatch.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMainCheckMatch newInstance(String param1, String param2) {
        FragmentMainCheckMatch fragment = new FragmentMainCheckMatch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMainCheckMatch() {
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
        view = inflater.inflate(R.layout.fragment_fragment_main_check_match, container, false);
        setSearchFab();
        setSpinner();

        refreshView = (PullToRefreshExpandableListView)view.findViewById(R.id.pulltorefresh);
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ExpandableListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                 String keyword = mAdapter.getKeyword();
                 searchMatch(filter, keyword);
            }
        });
//        getMoreItem 부분 search에서 조절하기
        refreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
//                getMoreMatches();
            }
        });

        listView = refreshView.getRefreshableView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        hView = new MVPview(getContext());
        searchMVP();
        hView.setOnHeaderImageListener(new MVPview.OnHeaderImageClickListener() {
            @Override
            public void onHeaderImageClick(MVPview view, String tag) {
                if (tag.equals("MVP")) {
                    Toast.makeText(getContext(), hView.getItemMVP().memName, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MyProfileActivity.class);
                    startActivity(intent);
                } else if (tag.equals("SCR")) {
                    Toast.makeText(getContext(), hView.getItemScr().memName, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MyProfileActivity.class);
                    startActivity(intent);
                } else if (tag.equals("CLUB")) {
                    Toast.makeText(getContext(), hView.getItemWin().clubName, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), FCActivity.class);
                    startActivity(intent);
                }
            }
        });
        listView.addHeaderView(hView);

        mAdapter = new MatchInfoAdapter();
        listView.setAdapter(mAdapter);
        listView.setGroupIndicator(null);
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                listView.expandGroup(groupPosition);
            }
        });
        expandGroup();

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (((SearchMatchItemView) v).getVisibilityLayout() == View.GONE) {
                    ((SearchMatchItemView) v).setVisible();

                } else {
                    ((SearchMatchItemView) v).setInvisible();

                }

                return true;
            }
        });
        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                String s = ((SearchMatchGroupItemView)v).getTextView().getText().toString();
                if(s.equals("더보기1")){
                    Toast.makeText(getContext(),"더보기1 page 추가", Toast.LENGTH_SHORT).show();
                    if(mAdapter.getTotalFuturePage()>mAdapter.getFuturePage()){
                        getMoreMatch(filter, keywordView.getText().toString());
                    }else{
                        Toast.makeText(getContext(),"더보기1 추가 내용 없음", Toast.LENGTH_SHORT).show();
                    }

                }else if(s.equals("더보기2")){
                    Toast.makeText(getContext(),"더보기2 page 추가", Toast.LENGTH_SHORT).show();
                    if(mAdapter.getTotalIngPage()> mAdapter.getIngPage()){
                        getMoreMatch(filter, keywordView.getText().toString());
                    }else{
                        Toast.makeText(getContext(),"더보기2 추가 내용 없음", Toast.LENGTH_SHORT).show();
                    }
                }else if(s.equals("더보기3")){
                    Toast.makeText(getContext(),"더보기3 page 추가", Toast.LENGTH_SHORT).show();
                    if(mAdapter.getTotalEndPage()> mAdapter.getEndPage()){
                        getMoreMatch(filter, keywordView.getText().toString());
                    }else {
                        Toast.makeText(getContext(),"더보기3 추가 내용 없음", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        keywordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                searchMatch(filter, s.toString());
//                getMoreMatches();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mAdapter.setOnAdapterExtraButtonClickListener(new MatchInfoAdapter.OnAdapterExtraButtonClickListener() {
            @Override
            public void setOnExtraButtonClick(ExpandableListAdapter adapter, View view) {
                Intent intent = new Intent(getContext(), FinalPositionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void searchMatchWithGroup(int position) {
        if(position ==0){
//            mAdapter.clear();
//            CheckMatchListGroupItem groupItem = new CheckMatchListGroupItem();
//            groupItem.text ="예정된 매치";
//            groupItem.color= 0xffc0c0c0;
            filter="date";
            searchMatch(filter, keywordView.getText().toString());
//            mAdapter.add("예정된 매치",);
//
//            filter ="date";

//                    groupItem = new CheckMatchListGroupItem();
//                    groupItem.text ="기록 대기중인 매치";
//                    groupItem.color= 0xffc0c0c0;
//                    mAdapter.add(groupItem);
//                    filter="ingMat";
//                    searchMatch(filter, keywordView.getText().toString());

//                    groupItem = new CheckMatchListGroupItem();
//                    groupItem.text ="마무리된 매치";
//                    groupItem.color= 0xffc0c0c0;
//                    mAdapter.add(groupItem);
//                    filter="endMat";
//                    searchMatch(filter, keywordView.getText().toString());

        }else if(position ==1){
//            mAdapter.clear();
//            CheckMatchListGroupItem groupItem = new CheckMatchListGroupItem();
//            groupItem.text ="내가 뛰었던 매치";
//            groupItem.color= 0xffc0c0c0;
//            mAdapter.add(groupItem);
            filter="myMat";
            searchMatch(filter, keywordView.getText().toString());
        }else if(position==2){
//            mAdapter.clear();
//            CheckMatchListGroupItem groupItem = new CheckMatchListGroupItem();
//            groupItem.text ="예정된 매치";
//            groupItem.color= 0xffc0c0c0;
//            mAdapter.add(groupItem);
            filter="futureMat";
            searchMatch(filter, keywordView.getText().toString());
        }else if(position==3){
//            mAdapter.clear();
//            CheckMatchListGroupItem groupItem = new CheckMatchListGroupItem();
//            groupItem.text ="마무리된 매치";
//            groupItem.color= 0xffc0c0c0;
//            mAdapter.add(groupItem);
            filter="endMat";
            searchMatch(filter, keywordView.getText().toString());
        }else if(position==4){
//            mAdapter.clear();
//            CheckMatchListGroupItem groupItem = new CheckMatchListGroupItem();
//            groupItem.text ="기록 대기중인 매치";
//            groupItem.color= 0xffc0c0c0;
//            mAdapter.add(groupItem);
            filter="ingMat";
            searchMatch(filter, keywordView.getText().toString());
        }
    }

//    private void getMoreMatches() {
//        if(filter.equals("date")){
//            if(isMoreFutureItem==true){
//                isMoreFutureItem = getMoreItem("futureMat");
//                if (!isMoreFutureItem) {
//                    //마무리된 매치 추가
//                    CheckMatchListGroupItem groupItem = new CheckMatchListGroupItem();
//                    groupItem.text = "입력 대기중인 매치";
//                    groupItem.color = 0xffc0c0c0;
////                    mAdapter.add(groupItem);
//                    searchMatch("ingMat", keywordView.getText().toString());
//                }
//            }
//            if(isMoreIngItem==true&&isMoreFutureItem==false){
//                isMoreIngItem = getMoreItem("ingMat");
//                if (!isMoreIngItem) {
//                    //마무리된 매치 추가
//                    CheckMatchListGroupItem groupItem = new CheckMatchListGroupItem();
//                    groupItem.text = "마무리된 매치";
//                    groupItem.color = 0xffc0c0c0;
////                    mAdapter.add(groupItem);
//                    searchMatch("endMat", keywordView.getText().toString());
//                }
//                if(isMoreEndItem==true&&isMoreFutureItem==false&&isMoreIngItem==false){
//                    isMoreIngItem = getMoreItem("endMat");
//                }
//            }
//        }else if(filter.equals("myMat")){
//            getMoreItem(filter);
//        }else if(filter.equals("futureMat")){
//            getMoreItem(filter);
//        }else if(filter.equals("endMat")){
//            getMoreItem(filter);
//        }else if(filter.equals("ingMat")){
//            getMoreItem(filter);
//        }
//    }


    private void searchMVP() {
       NetworkManager.getInstance().getNetworkMatchInfoMVP(getContext(), new NetworkManager.OnResultListener<MVP>() {
           @Override
           public void onSuccess(MVP result) {
               hView.setMVP(result);
           }

           @Override
           public void onFail(int code) {
               Toast.makeText(getContext(), "MVP 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
           }
       });

    }

    private void setSpinner() {
        spinner = (Spinner)view.findViewById(R.id.spinner);
        mySpinnerAdapter = new MySpinnerAdapter(getContext());
        spinner.setAdapter(mySpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "position : " + position, Toast.LENGTH_SHORT).show();
                searchMatchWithGroup(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        initSpinnerData();
    }

    private void initSpinnerData() {
        String[] items = getResources().getStringArray(R.array.items_search_match);
        for (String s : items) {
            mySpinnerAdapter.add(s);
        }
    }

    private void setSearchFab() {
        keywordView = (EditText) view.findViewById(R.id.custom_search_bar_editText);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearLayout.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
                (new Handler()).postDelayed(new Runnable() {

                    public void run() {

                        keywordView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                        keywordView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));

                    }
                }, 200);
            }
        });
        mLinearLayout = (LinearLayout)view.findViewById(R.id.custom_search_bar);
        mLinearLayout.setVisibility(View.GONE);
        keywordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    MyApplication.getmIMM().hideSoftInputFromWindow(keywordView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                }
                return true;
            }
        });
        Button btn = (Button)view.findViewById(R.id.custom_search_bar_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordView.setText("");
                MyApplication.getmIMM().hideSoftInputFromWindow(keywordView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                mLinearLayout.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
            }
        });
        btn = (Button)view.findViewById(R.id.custom_search_bar_button_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordView.setText("");
            }
        });
        keywordView.setText("");
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
    public void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());
    }



    MVPview.OnHeaderImageClickListener mHeaderImageListener;
    @Override
    public void onHeaderImageClick(MVPview view, String tag) {
        if (mHeaderImageListener != null) {
            mHeaderImageListener.onHeaderImageClick(view, tag);
        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        Log.d("hello", "매치 찾기 힌트");
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            getActivity().setTitle("매치 확인");
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        this.setUserVisibleHint(true);
    }

    private void searchMatch(final String filter,final String keyword) {
        //date일 경우만 따로 처리
        flag1 = false;
        flag2 = false;
        if(filter.equals("date")){
            NetworkManager.getInstance().getNetworkMatchInfo(getContext(), "futureMat", keyword, 1, 1, new NetworkManager.OnResultListener<MatchInfo>() {
                @Override
                public void onSuccess(MatchInfo result) {

                    mAdapter.setKeyword(keyword);
                    mAdapter.setFilter("futureMat");
                    mAdapter.setTotalCount(result.itemCount);
                    mAdapter.setFuturePage(1);
                    mAdapter.setTotalFuturePage(result.matPage);
//                    mAdapter.setTotalCount(result.total);
                    mAdapter.clear();

                    mAdapter.add("futureMat", result.items);

                    mAdapter.add("더보기1", null);
                    expandGroup();
                    refreshView.onRefreshComplete();
//                    if(result.matPage)
                    flag1 = true;
                    if (flag1) {
                        NetworkManager.getInstance().getNetworkMatchInfo(getContext(), "ingMat", keyword, 1, 1, new NetworkManager.OnResultListener<MatchInfo>() {
                            @Override
                            public void onSuccess(MatchInfo result) {
                                mAdapter.setKeyword(keyword);
                                mAdapter.setFilter("ingMat");
                                mAdapter.setTotalCount(result.itemCount);
                                mAdapter.setIngPage(1);
                                mAdapter.setTotalFuturePage(result.matPage);
//                    mAdapter.setTotalCount(result.total);
//                    mAdapter.clear();

                                mAdapter.add("ingMat", result.items);
                                mAdapter.add("더보기2", null);
                                expandGroup();
                                refreshView.onRefreshComplete();
                                flag2 = true;
                                if (flag2) {
                                    NetworkManager.getInstance().getNetworkMatchInfo(getContext(), "endMat", keyword, 1, 1, new NetworkManager.OnResultListener<MatchInfo>() {
                                        @Override
                                        public void onSuccess(MatchInfo result) {
                                            mAdapter.setKeyword(keyword);
                                            mAdapter.setFilter("endMat");
                                            mAdapter.setTotalCount(result.itemCount);
                                            mAdapter.setEndPage(1);
                                            mAdapter.setTotalEndPage(result.matPage);
//                    mAdapter.setTotalCount(result.total);
//                    mAdapter.clear();
                                            mAdapter.add("endMat", result.items);
                                            mAdapter.add("더보기3", null);
                                            expandGroup();
                                            refreshView.onRefreshComplete();

                                        }

                                        @Override
                                        public void onFail(int code) {
                                            Toast.makeText(getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onFail(int code) {
                                Toast.makeText(getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }

                @Override
                public void onFail(int code) {
                    Toast.makeText(getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                }
            });

        }else {

            NetworkManager.getInstance().getNetworkMatchInfo(getContext(), filter, keyword, 1, 1, new NetworkManager.OnResultListener<MatchInfo>() {
                @Override
                public void onSuccess(MatchInfo result) {
                    mAdapter.setKeyword(keyword);
                    mAdapter.setFilter(filter);
                    mAdapter.setTotalCount(result.itemCount);
                    mAdapter.setPage(1);
//                    mAdapter.setTotalCount(result.total);
                    mAdapter.clear();
                    mAdapter.add(filter, result.items);
                    expandGroup();
                    refreshView.onRefreshComplete();

                }

                @Override
                public void onFail(int code) {
                    Toast.makeText(getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                }
            });
        }
     }


    private void getMoreMatch(final String filter,final String keyword) {

        if (!isUpdate) {
            final String oldKeyword = mAdapter.getKeyword();

                isUpdate = true;
                //date일 경우만 따로 처리
                flag1 = false;
                flag2 = false;
                if (filter.equals("date")) {
                    NetworkManager.getInstance().getNetworkMatchInfo(getContext(), "futureMat", oldKeyword, mAdapter.getFuturePage(), 1, new NetworkManager.OnResultListener<MatchInfo>() {
                        @Override
                        public void onSuccess(MatchInfo result) {

                            mAdapter.setKeyword(oldKeyword);
                            mAdapter.setFilter("futureMat");
                            mAdapter.setTotalCount(result.itemCount);
                            mAdapter.setFuturePage(mAdapter.getFuturePage() + 1);
                            mAdapter.setTotalFuturePage(result.matPage);
//                    mAdapter.setTotalCount(result.total);
//                            mAdapter.clear();

                            mAdapter.add("futureMat", result.items);

                            mAdapter.add("더보기1", null);
                            expandGroup();
                            refreshView.onRefreshComplete();
//                    if(result.matPage)
                            flag1 = true;
                            if (flag1) {
                                NetworkManager.getInstance().getNetworkMatchInfo(getContext(), "ingMat", oldKeyword, mAdapter.getIngPage(), 1, new NetworkManager.OnResultListener<MatchInfo>() {
                                    @Override
                                    public void onSuccess(MatchInfo result) {
                                        mAdapter.setKeyword(oldKeyword);
                                        mAdapter.setFilter("ingMat");
                                        mAdapter.setTotalCount(result.itemCount);
                                        mAdapter.setIngPage(mAdapter.getIngPage() + 1);
                                        mAdapter.setTotalFuturePage(result.matPage);
//                    mAdapter.setTotalCount(result.total);
//                    mAdapter.clear();

                                        mAdapter.add("ingMat", result.items);
                                        mAdapter.add("더보기2", null);
                                        expandGroup();
                                        refreshView.onRefreshComplete();
                                        flag2 = true;
                                        if (flag2) {
                                            NetworkManager.getInstance().getNetworkMatchInfo(getContext(), "endMat", oldKeyword, mAdapter.getEndPage(), 1, new NetworkManager.OnResultListener<MatchInfo>() {
                                                @Override
                                                public void onSuccess(MatchInfo result) {
                                                    mAdapter.setKeyword(oldKeyword);
                                                    mAdapter.setFilter("endMat");
                                                    mAdapter.setTotalCount(result.itemCount);
                                                    mAdapter.setEndPage(mAdapter.getEndPage() + 1);
                                                    mAdapter.setTotalEndPage(result.matPage);
//                    mAdapter.setTotalCount(result.total);
//                    mAdapter.clear();
                                                    mAdapter.add("endMat", result.items);
                                                    mAdapter.add("더보기3", null);
                                                    expandGroup();
                                                    refreshView.onRefreshComplete();
                                                    isUpdate = false;

                                                }

                                                @Override
                                                public void onFail(int code) {
                                                    Toast.makeText(getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                                                    isUpdate = false;
                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public void onFail(int code) {
                                        Toast.makeText(getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                                        isUpdate = false;
                                    }
                                });
                            }

                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                            isUpdate = false;
                        }
                    });

                } else {
                    int nextPage = mAdapter.getNextPage();
                    if (nextPage != -1) {

                    NetworkManager.getInstance().getNetworkMatchInfo(getContext(), filter, oldKeyword, nextPage, 1, new NetworkManager.OnResultListener<MatchInfo>() {
                        @Override
                        public void onSuccess(MatchInfo result) {
                            mAdapter.setKeyword(oldKeyword);
                            mAdapter.setFilter(filter);
                            mAdapter.setTotalCount(result.itemCount);
   //                    mAdapter.setTotalCount(result.total);
                            mAdapter.clear();
                            mAdapter.add(filter, result.items);
                            expandGroup();
                            refreshView.onRefreshComplete();
                            isUpdate = false;

                        }

                        @Override
                        public void onFail(int code) {
                            Toast.makeText(getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                            isUpdate = false;
                        }
                    });
                }
            }
        }

    }




//    private boolean getMoreItem(String tag) {
//        if (!isUpdate) {
//            String keyword = mAdapter.getKeyword();
//            final String filter = mAdapter.getFilter();
//            int nextPage = mAdapter.getNextPage(tag);
//            if (nextPage != -1) {
//                isUpdate = true;
//                NetworkManager.getInstance().getNetworkMatchInfo(getContext(), filter, keyword, nextPage, 1, new NetworkManager.OnResultListener<MatchInfo>() {
//                    @Override
//                    public void onSuccess(MatchInfo result) {
//                        mAdapter.add(filter, result.items);
//                        isUpdate = false;
//                    }
//
//                    @Override
//                    public void onFail(int code) {
//                        isUpdate = false;
//                    }
//                });
//            }
//        }
//        if(mAdapter.getPage()==-1){
//            return false;
//        }
//        else
//            return true;
//
//
//    }
    public void expandGroup(){
        for(int i=0; i <mAdapter.getGroupCount(); i++){
            listView.expandGroup(i);
        }
    }

}
