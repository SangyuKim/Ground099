package com.android.ground.ground.controller.person.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.person.finalposition.FinalPositionActivity;
import com.android.ground.ground.manager.NetworkManager;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.naver.MovieAdapter;
import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.naver.MovieItemView;
import com.android.ground.ground.model.naver.NaverMovies;
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
    /*네이버 예제*/
    EditText keywordView;
    ListView listView;
    LinearLayout mLinearLayout;
    PullToRefreshListView refreshView;
    //    SwipeRefreshLayout refreshLayout;
    MovieAdapter mAdapter;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_main_search_player, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
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
                Toast.makeText(getContext(), "개인 프로필", Toast.LENGTH_SHORT).show();
            }
        });



        mAdapter = new MovieAdapter();
        listView.setAdapter(mAdapter);
        keywordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchMovie(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mAdapter.setOnAdapteRequestButtonListener(new MovieAdapter.OnAdapterRequestButtonListener() {
            @Override
            public void onAdapterRequestButtonClick(MovieAdapter adapter, MovieItemView view, MovieItem data) {
                Toast.makeText(getContext(), "영입하기", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
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