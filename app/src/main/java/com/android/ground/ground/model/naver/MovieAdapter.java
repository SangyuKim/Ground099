package com.android.ground.ground.model.naver;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class MovieAdapter extends BaseAdapter implements MovieItemView.OnExtraButtonClickListener, MovieItemView.OnRequestButtonClickListener {

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
        MovieItemView view;
        if (convertView == null) {
            view = new MovieItemView(parent.getContext());
            view.setOnExtraButtonListener(this);
            view.setOnRequestButtonListener(this);
        } else {
            view = (MovieItemView)convertView;
            view.setInvisible();
        }
        view.setMovieItem(items.get(position));
        return view;
    }

    public interface OnAdapterExtraButtonListener {
        public void onAdapterExtraButtonClick(MovieAdapter adapter, MovieItemView view, MovieItem data);
    }
    OnAdapterExtraButtonListener mListener;
    public void setOnAdapterExtraButtonListener(OnAdapterExtraButtonListener listener) {
        mListener = listener;
    }

    @Override
    public void onExtraButtonClick(MovieItemView view, MovieItem data) {
        if (mListener != null) {
            mListener.onAdapterExtraButtonClick(this, view, data);
        }
    }
    public interface OnAdapterRequestButtonListener {
        public void onAdapterRequestButtonClick(MovieAdapter adapter, MovieItemView view, MovieItem data);
    }
    OnAdapterRequestButtonListener mRequestListener;
    public void setOnAdapteRequestButtonListener(OnAdapterRequestButtonListener listener) {
        mRequestListener = listener;
    }
    @Override
    public void onRequestButtonClick(MovieItemView view, MovieItem data) {
        if (mRequestListener != null) {
            mRequestListener.onAdapterRequestButtonClick(this, view, data);
        }
    }
}
