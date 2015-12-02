package com.android.ground.ground.controller.fc.fcmain.ReadymatchResult;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-12-02.
 */
public class MyReadyMatchSpinnerAdapter extends BaseAdapter {
    List<String> items = new ArrayList<String>();
    AbsListView.LayoutParams lp;
    public void add(String s) {
        items.add(s);
        notifyDataSetChanged();
    }

    public MyReadyMatchSpinnerAdapter(Context context){
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_simple_ready_match_spinner_layout, null);
            tv = (TextView)view.findViewById(R.id.textView23);
            imageView =(ImageView)view.findViewById(R.id.imageView4);
        }

        tv.setText(items.get(position));
        tv.setGravity(Gravity.CENTER);
        view.setLayoutParams(lp);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        View view;
        ImageView imageView;
        if (convertView != null) {
            view = convertView;
            tv = (TextView)view.findViewById(R.id.textView23);
            imageView =(ImageView)view.findViewById(R.id.imageView4);

        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_simple_spinner_layout, null);
            tv = (TextView)view.findViewById(R.id.textView23);
//            view.setLayoutParams(new Spinner.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
//                    , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
//                    MyApplication.getContext().getResources().getDisplayMetrics())));
            imageView =(ImageView)view.findViewById(R.id.imageView4);
        }
        imageView.setVisibility(View.INVISIBLE);
        tv.setText(items.get(position));
        tv.setGravity(Gravity.CENTER);
        //todo

        AbsListView.LayoutParams mLp = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT
                , (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30,
                MyApplication.getContext().getResources().getDisplayMetrics()));

        view.setLayoutParams(mLp);
//        float density = getApplicationContext().getResources().getDisplayMetrics().density;
//        view.setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.spinner, MyApplication.getContext().getTheme().get R.style.Theme_App_Spinner));

        return view;
    }
}
