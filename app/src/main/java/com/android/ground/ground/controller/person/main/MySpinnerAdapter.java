package com.android.ground.ground.controller.person.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.ground.ground.controller.person.login.MySpinnerSignupAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-06.
 */
public class MySpinnerAdapter extends BaseAdapter {
    List<String> items = new ArrayList<String>();
    AbsListView.LayoutParams lp;
    public void add(String s) {
        items.add(s);
        notifyDataSetChanged();
    }

    public MySpinnerAdapter(Context context){
        lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);

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
        TextView tv;
        if (convertView != null) {
            tv = (TextView)convertView;
        } else {
            tv = (TextView)LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
        }
        tv.setText(items.get(position));
        tv.setLayoutParams(lp);
        return tv;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        if (convertView != null) {
            tv = (TextView)convertView;
        } else {
            tv = (TextView)LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
            tv.setLayoutParams(new Spinner.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//            tv.setBackgroundColor(Color.YELLOW);
        }
        tv.setText(items.get(position));
        tv.setLayoutParams(lp);
        return tv;
    }
}
