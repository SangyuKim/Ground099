package com.android.ground.ground.controller.person.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.person.main.matchinfo.CheckMatchListData;
import com.android.ground.ground.model.person.main.matchinfo.CheckMatchListGroupItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class SearchMatchAdapter extends BaseAdapter {

    List<CheckMatchListData> items = new ArrayList<CheckMatchListData>();


    String keyword;
    int totalCount;
    private int checked;

    private static final int TYPE_INDEX_CHILD = 0;
    private static final int TYPE_INDEX_GROUP = 1;

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

    public void add(CheckMatchListData item) {
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
//        switch (getItemViewType(position)) {
//            case TYPE_INDEX_CHILD: {
//                SearchMatchItemView view;
//                if (convertView != null && convertView instanceof SearchMatchItemView) {
//                    view = (SearchMatchItemView)convertView;
//                } else {
//                    view = new SearchMatchItemView(parent.getContext());
//                 }
//
//                view.setMovieItem((MovieItem)items.get(position));
//                return null;
//
//            }
//            case TYPE_INDEX_GROUP:{
//                 SearchMatchGroupItemView view;
//
//                if (convertView != null && convertView instanceof SearchMatchGroupItemView) {
//                    view = (SearchMatchGroupItemView)convertView;
//                } else {
//                    view = new SearchMatchGroupItemView(parent.getContext());
//                }
//
//                view.setGroupItem((CheckMatchListGroupItem) items.get(position));
//                return view;
//
//            }
//        }//switch
            return null;
        }

        @Override
        public int getItemViewType ( int position){
            CheckMatchListData d = items.get(position);
            if (d instanceof CheckMatchListGroupItem) {
                return TYPE_INDEX_GROUP;
            } else if (d instanceof MovieItem) {
                return TYPE_INDEX_CHILD;
            }

            return super.getItemViewType(position);
        }
    }
