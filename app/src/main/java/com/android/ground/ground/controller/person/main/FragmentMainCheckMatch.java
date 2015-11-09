package com.android.ground.ground.controller.person.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.naver.NaverMovies;
import com.android.ground.ground.model.person.main.CheckMatchListGroupItem;
import com.android.ground.ground.view.person.main.MVPview;
import com.android.ground.ground.view.person.main.SearchMatchTestItemView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
    boolean isMoreList = true;

    View view;
    FloatingActionButton fab;
    Spinner spinner;
    MySpinnerAdapter mySpinnerAdapter;
    /*네이버 예제*/
    EditText keywordView;
    ListView listView;
    LinearLayout mLinearLayout;
    PullToRefreshListView refreshView;
    //    SwipeRefreshLayout refreshLayout;
    SearchMatchAdapter mAdapter;
    private static final boolean isNaverMovie = true;
    boolean isUpdate = false;
    boolean isLastItem = false;

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

        refreshView = (PullToRefreshListView)view.findViewById(R.id.pulltorefresh);
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String keyword = mAdapter.getKeyword();
                searchMovie(keyword);
            }
        });
        refreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isMoreList = getMoreItem();
                if(!isMoreList){
                    //마무리된 매치 추가
                    CheckMatchListGroupItem groupItem = new CheckMatchListGroupItem();
                    groupItem.text ="마무리된 매치";
                    groupItem.color= 0xffc0c0c0;
                    mAdapter.add(groupItem);
                    searchMovie2("사랑");
                }
            }
        });

        listView = refreshView.getRefreshableView();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        MVPview hView = new MVPview(getContext());
        hView.setOnHeaderImageListener(new MVPview.OnHeaderImageClickListener() {
            @Override
            public void onHeaderImageClick(MVPview view, Profile data) {
                if (data instanceof MyProfileActivity) {
                    Intent intent = new Intent(getContext(), MyProfileActivity.class);
                    startActivity(intent);
                } else if (data instanceof FCActivity) {
                    Intent intent = new Intent(getContext(), FCActivity.class);
                    startActivity(intent);
                }
            }
        });
        listView.addHeaderView(hView);

        mAdapter = new SearchMatchAdapter();
        listView.setAdapter(mAdapter);



        if(keywordView.getText().toString().equals("")){
            searchMovie("매치");
        }
        keywordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    searchMovie("매치");
                }
                searchMovie(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mAdapter.setOnAdapterExtraButtonListener(new SearchMatchAdapter.OnAdapterExtraButtonListener() {
            @Override
            public void onAdapterExtraButtonClick(SearchMatchAdapter adapter, SearchMatchTestItemView view, MovieItem data) {
                   Intent intent = new Intent(getContext(), FinalPositionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setSpinner() {
        spinner = (Spinner)view.findViewById(R.id.spinner);
        mySpinnerAdapter = new MySpinnerAdapter();
        spinner.setAdapter(mySpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "position : " + position, Toast.LENGTH_SHORT).show();
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
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearLayout.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
            }
        });
        mLinearLayout = (LinearLayout)view.findViewById(R.id.custom_search_bar);
        mLinearLayout.setVisibility(View.GONE);
        keywordView = (EditText) view.findViewById(R.id.custom_search_bar_editText);
        keywordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    MyApplication.getmIMM().hideSoftInputFromWindow(keywordView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mLinearLayout.setVisibility(View.GONE);
                    fab.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
        Button btn = (Button)view.findViewById(R.id.custom_search_bar_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getmIMM().hideSoftInputFromWindow(keywordView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                mLinearLayout.setVisibility(View.GONE);
                fab.setVisibility(View.VISIBLE);
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
    private boolean getMoreItem() {

        if (!isUpdate) {

            String keyword = mAdapter.getKeyword();
            int startIndex = mAdapter.getStartIndex();
            if( startIndex == -1){
                return false;
            }

            if (!TextUtils.isEmpty(keyword) && startIndex != -1) {

                isUpdate = true;
                NetworkManager.getInstance().getNetworkMelon(getContext(), keyword, startIndex, 10, new NetworkManager.OnResultListener<NaverMovies>() {
                    @Override
                    public void onSuccess(NaverMovies result) {

                        for (MovieItem item : result.items) {
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
        }//semaphore
        return true;
    }
    private void searchMovie(final String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            NetworkManager.getInstance().getNetworkMelon(getContext(), keyword, 1, 10, new NetworkManager.OnResultListener<NaverMovies>() {
                @Override
                public void onSuccess(NaverMovies result) {
                    mAdapter.setKeyword(keyword);
                    mAdapter.setTotalCount(result.total);
                    mAdapter.clear();
                    CheckMatchListGroupItem groupItem = new CheckMatchListGroupItem();
                    groupItem.text ="예정된 매치";
                    groupItem.color= 0xffc0c0c0;
                    mAdapter.add(groupItem);

                    for (MovieItem item : result.items) {
                        mAdapter.add(item);
                    }

                    refreshView.onRefreshComplete();

                }

                @Override
                public void onFail(int code) {
                    Toast.makeText(getContext(), "error : " + code, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mAdapter.clear();
            mAdapter.setKeyword(keyword);
        }
    }
    private void searchMovie2(final String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            NetworkManager.getInstance().getNetworkMelon(getContext(), keyword, 1, 10, new NetworkManager.OnResultListener<NaverMovies>() {
                @Override
                public void onSuccess(NaverMovies result) {
                    mAdapter.setKeyword(keyword);
                    mAdapter.setTotalCount(result.total);

                    for (MovieItem item : result.items) {
                        mAdapter.add(item);
                    }

                    refreshView.onRefreshComplete();

                }

                @Override
                public void onFail(int code) {
                    Toast.makeText(getContext(), "error : " + code, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mAdapter.clear();
            mAdapter.setKeyword(keyword);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());
    }



    MVPview.OnHeaderImageClickListener mHeaderImageListener;
    @Override
    public void onHeaderImageClick(MVPview view, Profile data) {
        if (mHeaderImageListener != null) {
            mHeaderImageListener.onHeaderImageClick(view, data);
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
}
