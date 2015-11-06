package com.android.ground.ground.model.tmap;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.naver.MovieItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class TmapAdapter extends BaseAdapter  {

    List<DongInfo> items = new ArrayList<DongInfo>();

    String keyword;
    String totalCount;

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public String getKeyword() {
        return keyword;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }
    public String getTotalCount() {
        return totalCount;
    }

    public int getStartIndex() {
        if (items.size() < Integer.parseInt(totalCount)) {
            return items.size() + 1;
        }
        return -1;
    }

    public void add(DongInfo item) {
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
        TmapItemView view;
        if (convertView == null) {
            view = new TmapItemView(parent.getContext());

        } else {
            view = (TmapItemView)convertView;

        }
        view.setTmapItem(items.get(position));
        return view;
    }


}
