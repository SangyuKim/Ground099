package com.android.ground.ground.controller.person.main;

import android.content.Context;
import android.graphics.Color;
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
import com.android.ground.ground.controller.person.login.MySpinnerSignupAdapter;

import java.util.ArrayList;
import java.util.List;

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
        ImageView imageView;
        View view;
        if (convertView != null) {
            view = convertView;
            tv = (TextView)view.findViewById(R.id.textView23);
            imageView =(ImageView)view.findViewById(R.id.imageView4);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_simple_spinner_layout, null);
            tv = (TextView)view.findViewById(R.id.textView23);
            imageView =(ImageView)view.findViewById(R.id.imageView4);
        }

        tv.setText(items.get(position));
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        view.setLayoutParams(lp);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        View view;
        ImageView imageView;
        if (convertView != null) {
//            tv = (TextView)convertView;
            view = convertView;
            tv = (TextView)view.findViewById(R.id.textView23);
            imageView =(ImageView)view.findViewById(R.id.imageView4);

        } else {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_simple_spinner_layout, null);
            tv = (TextView)view.findViewById(R.id.textView23);
//            tv = (TextView)LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_simple_list_item_spinner, null);
            view.setLayoutParams(new Spinner.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView =(ImageView)view.findViewById(R.id.imageView4);
//            tv.setBackgroundColor(Color.YELLOW);
        }
        imageView.setVisibility(View.INVISIBLE);
        tv.setText(items.get(position));
        tv.setGravity(Gravity.CENTER_HORIZONTAL);
        view.setLayoutParams(lp);
        return view;
    }
}
