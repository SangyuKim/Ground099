package com.android.ground.ground.controller.person.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.ground.ground.model.person.main.AlarmItemData;
import com.android.ground.ground.view.person.main.AlarmItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class MainAlarmAdapter extends BaseAdapter implements AlarmItemView.OnImageClickListener {

    List<AlarmItemData> items = new ArrayList<AlarmItemData>();

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
        AlarmItemView view;
        if (convertView == null) {
            view =  new AlarmItemView(parent.getContext());
            view.setOnImageClickListener(this);
        } else {
            view = (AlarmItemView) convertView;
        }
        view.setAlarmItemData(items.get(position));
        return view;
    }

    @Override
    public void onImageClick(AlarmItemView view, AlarmItemData data) {

    }
    public interface OnAdapterImageListener {
        public void onAdapterImageClick(MainAlarmAdapter adapter, AlarmItemView view, AlarmItemData data);
    }
    OnAdapterImageListener mListener;
    public void setOnAdapterImageListener(OnAdapterImageListener listener) {
        mListener = listener;
    }
    AlarmItemView.OnImageClickListener mImageClickListener;
    public void setOnImageClickListener(AlarmItemView.OnImageClickListener listener) {
        mImageClickListener = listener;
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }

    public void add(AlarmItemData item) {
        items.add(item);
        notifyDataSetChanged();
    }
}
