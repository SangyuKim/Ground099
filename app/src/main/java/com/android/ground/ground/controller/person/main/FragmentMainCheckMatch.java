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
import android.util.Log;
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
import com.android.ground.ground.controller.person.profile.YourProfileActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.person.main.matchinfo.CheckMatchListGroupItem;
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
        setSpinner();
        setSearchFab();



        refreshView = (PullToRefreshExpandableListView)view.findViewById(R.id.pulltorefresh);
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ExpandableListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                 String keyword = mAdapter.getKeyword();
                if(keyword ==null){
                    keyword="";
                }
               searchMatch(filter, keyword);
            }
        });
//        getMoreItem 부분 search에서 조절하기
        refreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                getMoreMatch(filter, keywordView.getText().toString());
            }
        });

        listView = refreshView.getRefreshableView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        hView = new MVPview(getContext());
        searchMVP();
        hView.setOnHeaderImageListener(new MVPview.OnHeaderImageClickListener() {
            @Override
            public void onHeaderImageClick(MVPview view, String tag) {
                switch(tag){
                    case "MVP":{
                        //                    Toast.makeText(getContext(), hView.getItemMVP().memName, Toast.LENGTH_SHORT).show();
                        int id = view.itemMVP.member_id;


                        if(id == PropertyManager.getInstance().getMyPageResult().member_id){
                            Intent intent = new Intent(getContext(), MyProfileActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getContext(), YourProfileActivity.class);
                            intent.putExtra("memberId",id);
                            startActivity(intent);
                        }
                          break;
                    } case "SCR":{
                        //                    Toast.makeText(getContext(), hView.getItemScr().memName, Toast.LENGTH_SHORT).show();
                       int id =  view.itemScr.member_id;
                        if(id == PropertyManager.getInstance().getMyPageResult().member_id){
                            Intent intent = new Intent(getContext(), MyProfileActivity.class);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getContext(), YourProfileActivity.class);
                            intent.putExtra("memberId",id);
                            startActivity(intent);
                        }
                        break;
                    } case "CLUB":{
                        //                    Toast.makeText(getContext(), hView.getItemWin().clubName, Toast.LENGTH_SHORT).show();
                        int id = view.itemWin.club_id;
                        Intent intent = new Intent(getContext(), FCActivity.class);
                        intent.putExtra("clubId", id);
                        startActivity(intent);
                        break;
                    }
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
                    CheckMatchListGroupItem mGroupItem = (CheckMatchListGroupItem)mAdapter.getGroup(groupPosition);
                    if(mGroupItem.text.equals("종료된 매치")){
                        ((SearchMatchItemView) v).setVisible();
                    }
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
                switch(s){
                    case "예정된 매치 더보기":{
//                        Toast.makeText(getContext(),"예정된 매치 더보기 page 추가", Toast.LENGTH_SHORT).show();
                        if(mAdapter.getTotalFuturePage()>mAdapter.getFuturePage()){
                            getMoreMatch(filter, keywordView.getText().toString());
                        }else{
                            Toast.makeText(getContext(),"예정된 매치 더보기 추가 내용 없음", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case "기록 대기중 매치 더보기":{
//                        Toast.makeText(getContext(),"기록 대기중 매치 더보기 page 추가", Toast.LENGTH_SHORT).show();
                        if(mAdapter.getTotalIngPage()> mAdapter.getIngPage()){
                            getMoreMatch(filter, keywordView.getText().toString());
                        }else{
                            Toast.makeText(getContext(),"기록 대기중 매치 더보기 추가 내용 없음", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    case "종료된 매치 더보기":{
//                        Toast.makeText(getContext(),"종료된 매치 더보기 page 추가", Toast.LENGTH_SHORT).show();
                        if(mAdapter.getTotalEndPage()> mAdapter.getEndPage()){
                            getMoreMatch(filter, keywordView.getText().toString());
                        }else {
                            Toast.makeText(getContext(),"종료된 매치 더보기 추가 내용 없음", Toast.LENGTH_SHORT).show();
                        }
                        break;
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
                searchMatch(filter, s.toString());
           }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mAdapter.setOnAdapterExtraButtonClickListener(new MatchInfoAdapter.OnAdapterExtraButtonClickListener() {
            @Override
            public void setOnExtraButtonClick(ExpandableListAdapter adapter, View view) {

                Intent intent = new Intent(getContext(), FinalPositionActivity.class);
                intent.putExtra("matchId",((SearchMatchItemView) view).mItem.match_id );
                intent.putExtra("home_id", ((SearchMatchItemView) view).mItem.home_id);
                intent.putExtra("away_id", ((SearchMatchItemView) view).mItem.away_id);
                startActivity(intent);
            }
        });

        return view;
    }

    private void searchMatchWithGroup(int position) {
        if(position ==0){
            filter="date";
            searchMatch(filter, keywordView.getText().toString());
        }else if(position ==1){
            filter="myMat";
            searchMatch(filter, keywordView.getText().toString());
        }else if(position==2){
            filter="futureMat";
            searchMatch(filter, keywordView.getText().toString());
        }else if(position==3){
            filter="endMat";
            searchMatch(filter, keywordView.getText().toString());
        }else if(position==4){
            filter="ingMat";
            searchMatch(filter, keywordView.getText().toString());
        }
    }

    private void searchMVP() {
       NetworkManager.getInstance().getNetworkMatchInfoMVP(getContext(), new NetworkManager.OnResultListener<MVP>() {
           @Override
           public void onSuccess(MVP result) {
               hView.setMVP(result);
           }

           @Override
           public void onFail(int code) {
//               Toast.makeText(MyApplication.getContext(), "MVP 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
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
        filter = "date";
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
                searchMatch(filter, "");
            }
        });
        btn = (Button)view.findViewById(R.id.custom_search_bar_button_cancel);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordView.setText("");
                searchMatch(filter, "");
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
        void onFragmentInteraction(Uri uri);
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

    @Override
    public void onResume() {
        super.onResume();
        this.setUserVisibleHint(true);
    }

    private void searchMatch(final String filter,final String keyword) {

        if(!keyword.equals("")){

          NetworkManager.getInstance().getNetworkMatchInfo(getContext(), filter, keyword, 1, PropertyManager.getInstance().getUserId(), new NetworkManager.OnResultListener<MatchInfo>() {
              @Override
              public void onSuccess(MatchInfo result) {

                  mAdapter.setKeyword(keyword);
                  mAdapter.setTotalCount(result.itemCount);
                  mAdapter.setTotalFuturePage(result.matPage);
                  mAdapter.setPage(1);
                  mAdapter.clear();

                  mAdapter.add("검색 결과", result.items);
//                  Log.d("hello", "count  : " + result.itemCount);
                  expandGroup();
              }

              @Override
              public void onFail(int code) {

              }
          });
        }else {


            //date일 경우만 따로 처리
            flag1 = false;
            flag2 = false;
            if (filter.equals("date")) {
                NetworkManager.getInstance().getNetworkMatchInfo(getContext(), "futureMat", keyword, 1, PropertyManager.getInstance().getUserId(), new NetworkManager.OnResultListener<MatchInfo>() {
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

                        mAdapter.add("예정된 매치 더보기", null);
                        expandGroup();
                        refreshView.onRefreshComplete();
//                    if(result.matPage)
                        flag1 = true;
                        if (flag1) {
                            NetworkManager.getInstance().getNetworkMatchInfo(getContext(), "ingMat", keyword, 1, PropertyManager.getInstance().getUserId(), new NetworkManager.OnResultListener<MatchInfo>() {
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
                                    mAdapter.add("기록 대기중 매치 더보기", null);
                                    expandGroup();
                                    refreshView.onRefreshComplete();
                                    flag2 = true;
                                    if (flag2) {
                                        NetworkManager.getInstance().getNetworkMatchInfo(getContext(), "endMat", keyword, 1, PropertyManager.getInstance().getUserId(), new NetworkManager.OnResultListener<MatchInfo>() {
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
                                                mAdapter.add("종료된 매치 더보기", null);
                                                expandGroup();
                                                refreshView.onRefreshComplete();

                                            }

                                            @Override
                                            public void onFail(int code) {
//                                                Toast.makeText(MyApplication.getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onFail(int code) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onFail(int code) {
//                        Toast.makeText(MyApplication.getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                    }
                });

            } else {

                NetworkManager.getInstance().getNetworkMatchInfo(getContext(), filter, keyword, 1, PropertyManager.getInstance().getUserId(), new NetworkManager.OnResultListener<MatchInfo>() {
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
//                        Toast.makeText(MyApplication.getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                    }
                });
            }
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
                    isUpdate = false;
                } else {
                    int nextPage = mAdapter.getNextPage();
                    if (nextPage != -1) {

                    NetworkManager.getInstance().getNetworkMatchInfo(getContext(), filter, oldKeyword, nextPage, PropertyManager.getInstance().getUserId(), new NetworkManager.OnResultListener<MatchInfo>() {
                        @Override
                        public void onSuccess(MatchInfo result) {
                            mAdapter.setKeyword(oldKeyword);
                            mAdapter.setFilter(filter);
                            mAdapter.setTotalCount(result.itemCount);
                            mAdapter.clear();
                            mAdapter.add(filter, result.items);
                            expandGroup();
                            refreshView.onRefreshComplete();
                            isUpdate = false;
                        }

                        @Override
                        public void onFail(int code) {
//                            Toast.makeText(getContext(), "Search Match error code :  " + code, Toast.LENGTH_SHORT).show();
                            isUpdate = false;
                        }
                    });
                }
            }
        }//update flag

    }


    public void expandGroup(){
        for(int i=0; i <mAdapter.getGroupCount(); i++){
            listView.expandGroup(i);
        }
    }

}
