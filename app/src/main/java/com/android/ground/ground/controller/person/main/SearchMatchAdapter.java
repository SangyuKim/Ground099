package com.android.ground.ground.controller.person.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.naver.MovieItemView;
import com.android.ground.ground.model.person.message.MyMessageItem;
import com.android.ground.ground.view.person.main.MVPview;
import com.android.ground.ground.view.person.main.SearchFCTestItemView;
import com.android.ground.ground.view.person.main.SearchMatchTestItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class SearchMatchAdapter extends BaseAdapter implements SearchMatchTestItemView.OnExtraButtonClickListener{

    List<MovieItem> items = new ArrayList<MovieItem>();


    String keyword;
    int totalCount;
    private int checked;

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public String getKeyword() {
        return keyword;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    public int getTotalCount() {
        return totalCount;
    }

    public int getStartIndex() {
        if (items.size() < totalCount) {
            return items.size() + 1;
        }
        return -1;
    }

    public void add(MovieItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchMatchTestItemView view;
        if (convertView == null) {
            view = new SearchMatchTestItemView(parent.getContext());
            view.setOnExtraButtonListener(this);


        } else {
            view = (SearchMatchTestItemView)convertView;
        }
        view.setMovieItem(items.get(position));
        return view;
    }



    public interface OnAdapterExtraButtonListener {
        public void onAdapterExtraButtonClick(SearchMatchAdapter adapter, SearchMatchTestItemView view, MovieItem data);
    }
    OnAdapterExtraButtonListener mListener;
    public void setOnAdapterExtraButtonListener(OnAdapterExtraButtonListener listener) {
        mListener = listener;
    }

    @Override
    public void onExtraButtonClick(SearchMatchTestItemView view, MovieItem data) {
        if (mListener != null) {
            mListener.onAdapterExtraButtonClick(this, view, data);
        }
    }


}
