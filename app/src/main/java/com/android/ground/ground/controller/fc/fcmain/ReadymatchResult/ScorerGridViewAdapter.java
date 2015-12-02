package com.android.ground.ground.controller.fc.fcmain.ReadymatchResult;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualResResult;
import com.android.ground.ground.view.fc.fcmain.ReadyMatchResultListItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-12-02.
 */
public class ScorerGridViewAdapter extends BaseAdapter {
    List<String> items = new ArrayList<String>();
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
        ReadyMatchResultListItemView view;
        if (convertView == null) {
            view = new ReadyMatchResultListItemView(parent.getContext());
        } else {
            view = (ReadyMatchResultListItemView)convertView;
        }
        view.setText(items.get(position));
        return view;
    }
    public void add(String item){
        items.add(item);
        notifyDataSetChanged();
    }
    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }
}
