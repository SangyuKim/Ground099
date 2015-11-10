package com.android.ground.ground.controller.person.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.view.person.main.SearchPlayerItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class SearchPlayerTestAdapter extends BaseAdapter{

    List<MovieItem> items = new ArrayList<MovieItem>();

    String keyword;
    int totalCount;

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
//        SearchPlayerItemView view;
//        if (convertView == null) {
//            view = new SearchPlayerItemView(parent.getContext());
//            view.setOnRequestButtonListener(this);
//        } else {
//            view = (SearchPlayerItemView)convertView;
//        }
//        view.setMovieItem(items.get(position));
        return null;
    }

    public interface OnAdapterRequestButtonListener {
        public void onAdapterRequestButtonClick(SearchPlayerTestAdapter adapter, SearchPlayerItemView view, MovieItem data);
    }
    OnAdapterRequestButtonListener mRequestListener;
    public void setOnAdapteRequestButtonListener(OnAdapterRequestButtonListener listener) {
        mRequestListener = listener;
    }
//
//    @Override
//    public void onRequestButtonClick(SearchPlayerItemView view, MovieItem data) {
//        if (mRequestListener != null) {
//            mRequestListener.onAdapterRequestButtonClick(this, view, data);
//        }
//    }
}
