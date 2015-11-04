package com.android.ground.ground.controller.person.main;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.profile.MyProfileFragment;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.naver.NaverMovies;
import com.android.ground.ground.view.person.main.SearchPlayerTestItemView;
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
    SearchPlayerTestAdapter mAdapter;
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
                searchMovie(keyword);
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
                Fragment mFragment = (Fragment) MyProfileFragment.newInstance("", "");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, mFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        mAdapter = new SearchPlayerTestAdapter();
        listView.setAdapter(mAdapter);
        if(keywordView.getText().toString().equals("")){
            searchMovie("선수");
        }

        keywordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    searchMovie("선수");
                }
                searchMovie(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mAdapter.setOnAdapteRequestButtonListener(new SearchPlayerTestAdapter.OnAdapterRequestButtonListener() {
            @Override
            public void onAdapterRequestButtonClick(SearchPlayerTestAdapter adapter, SearchPlayerTestItemView view, MovieItem data) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("영입하기");
                builder.setMessage("영입 신청하시겠습니까? ");
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

        String[] items = getResources().getStringArray(R.array.items_search_player);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getActivity().setTitle("FourFragement");
        }
    }
    private void getMoreItem() {
        if (!isUpdate) {
            String keyword = mAdapter.getKeyword();
            int startIndex = mAdapter.getStartIndex();
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
        }
    }
    private void searchMovie(final String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            NetworkManager.getInstance().getNetworkMelon(getContext(), keyword, 1, 10, new NetworkManager.OnResultListener<NaverMovies>() {
                @Override
                public void onSuccess(NaverMovies result) {
                    mAdapter.setKeyword(keyword);
                    mAdapter.setTotalCount(result.total);
                    mAdapter.clear();
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

}
