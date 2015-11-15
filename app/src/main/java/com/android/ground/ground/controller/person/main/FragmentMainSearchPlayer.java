package com.android.ground.ground.controller.person.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.profile.MyProfileActivity;
import com.android.ground.ground.controller.person.profile.YourProfileActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.manager.PropertyManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.naver.NaverMovies;
import com.android.ground.ground.model.person.main.searchClub.SearchClub;
import com.android.ground.ground.model.person.main.searchClub.SearchClubResult;
import com.android.ground.ground.model.person.main.searchMem.SearchMem;
import com.android.ground.ground.model.person.main.searchMem.SearchMemAdapter;
import com.android.ground.ground.model.person.main.searchMem.SearchMemResult;
import com.android.ground.ground.view.OnAdapterSpecificDialogListener;
import com.android.ground.ground.view.person.main.SearchPlayerItemView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentMainSearchPlayer.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentMainSearchPlayer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMainSearchPlayer extends Fragment {

    //todo
    String filter;
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
    SearchMemAdapter mAdapter;
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
     * @return A new instance of fragment FragmentMainSearchPlayer.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMainSearchPlayer newInstance(String param1, String param2) {
        FragmentMainSearchPlayer fragment = new FragmentMainSearchPlayer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMainSearchPlayer() {
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

       view = inflater.inflate(R.layout.fragment_fragment_main_search_player, container, false);
        setSearchFab();
        setSpinner();

        refreshView = (PullToRefreshListView)view.findViewById(R.id.pulltorefresh);
        refreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String keyword = mAdapter.getKeyword();
                searchMem(filter, keyword);
            }
        });

        refreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                getMoreItem();
            }
        });

        listView = refreshView.getRefreshableView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyApplication.getmIMM().hideSoftInputFromWindow(keywordView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                int memberId = ((SearchPlayerItemView)view).getmItem().member_id;
                if(memberId == PropertyManager.getInstance().getMyPageResult().member_id){
                    Intent intent = new Intent(getContext(), MyProfileActivity.class);
                    startActivity(intent);
                }else{
                Intent intent = new Intent(getContext(), YourProfileActivity.class);
                intent.putExtra("memberId",memberId);
                startActivity(intent);
                }
            }
        });


        mAdapter = new SearchMemAdapter();
        listView.setAdapter(mAdapter);
         searchMem(filter, keywordView.getText().toString());


        keywordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchMem(filter,s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mAdapter.setOnAdapterSpecificDialogListener(new OnAdapterSpecificDialogListener() {
            @Override
            public void onAdapterDialogClick(Adapter adapter, View view, String tag) {
                MyApplication.getmIMM().hideSoftInputFromWindow(keywordView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("영입하기");
                int id = ((SearchPlayerItemView)view).getmItem().member_id;
                builder.setMessage( id + "선수를  "+" 영입 신청하시겠습니까? ");
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

    private void setSpinner() {
        spinner = (Spinner)view.findViewById(R.id.spinner);

        mySpinnerAdapter = new MySpinnerAdapter(getContext());
        spinner.setAdapter(mySpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "position : " + position, Toast.LENGTH_SHORT).show();
                if(position ==0){
                    filter ="dist";
                    searchMem(filter, keywordView.getText().toString());
                }
               else if(position ==1){
                    filter="weekend";
                    searchMem(filter, keywordView.getText().toString());
                }else if(position==2){
                    filter="clubYN";
                    searchMem(filter, keywordView.getText().toString());
                }else if(position==3){
                    filter="score";
                    searchMem(filter, keywordView.getText().toString());
                }else if(position==4){
                    filter="age";
                    searchMem(filter, keywordView.getText().toString());
                }else if(position==5){
                    filter="skill";
                    searchMem(filter, keywordView.getText().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        initSpinnerData();
    }

    private void initSpinnerData() {

        String[] items = getResources().getStringArray(R.array.items_search_player);
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
                if(actionId == EditorInfo.IME_ACTION_DONE){
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

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        Log.d("hello", "선수 찾기 힌트");
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            getActivity().setTitle("선수 찾기");
//        }
//    }
    private void getMoreItem() {
        if (!isUpdate) {
            String keyword = mAdapter.getKeyword();
            int nextPage = mAdapter.getNextPage();
            if (nextPage != -1) {
                isUpdate = true;
                NetworkManager.getInstance().getNetworkSearchMem(getContext(), filter, keyword, nextPage, 1, new NetworkManager.OnResultListener<SearchMem>() {
                    @Override
                    public void onSuccess(SearchMem result) {
                        for (SearchMemResult item : result.items) {
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
    private void searchMem(final String filter,final  String keyword) {
        if (!TextUtils.isEmpty(filter)) {
            NetworkManager.getInstance().getNetworkSearchMem(getContext(), filter, keyword, 1, 1, new NetworkManager.OnResultListener<SearchMem>() {
                @Override
                public void onSuccess(SearchMem result) {
                    mAdapter.setKeyword(keyword);
                    mAdapter.setFilter(filter);
                    mAdapter.setTotalCount(result.itemCount);
                    mAdapter.setPgae(1);
//                    mAdapter.setTotalCount(result.total);
                    mAdapter.clear();
                    for (SearchMemResult item : result.items) {
                        mAdapter.add(item);
                    }

                    refreshView.onRefreshComplete();

                }

                @Override
                public void onFail(int code) {
                    Toast.makeText(MyApplication.getContext(), "선수 찾기 error code : " + code, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mAdapter.clear();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(MyApplication.getContext());
    }


}
