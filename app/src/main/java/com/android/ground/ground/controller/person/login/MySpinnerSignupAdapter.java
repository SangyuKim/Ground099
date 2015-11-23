package com.android.ground.ground.controller.person.login;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.ground.ground.R;

import java.util.ArrayList;
import java.util.List;

public class MySpinnerSignupAdapter extends BaseAdapter {
    List<String> items = new ArrayList<String>();
    AbsListView.LayoutParams lp;

    public MySpinnerSignupAdapter(List<String> items) {
        this.items = items;
    }
    public MySpinnerSignupAdapter(Context context){
        lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
    }

    public void add(String s) {
        items.add(s);
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
        View view;
        TextView tv;
        ImageView imageView;
        if (convertView != null) {
//            tv = (TextView)convertView;
            view = convertView;
            tv = (TextView)view.findViewById(R.id.textView23);
            imageView = (ImageView)view.findViewById(R.id.imageView4);
        } else {
//            tv = (TextView)LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_simple_spinner_signup_layout, null);
            tv = (TextView)view.findViewById(R.id.textView23);
            imageView = (ImageView)view.findViewById(R.id.imageView4);
        }
        tv.setText(items.get(position));
        tv.setGravity(Gravity.CENTER);
        view.setLayoutParams(lp);
        return view;
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
