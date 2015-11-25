package com.android.ground.ground.controller.person.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.ground.ground.model.noti.NotiDataResult;
import com.android.ground.ground.model.person.main.AlarmItemData;
import com.android.ground.ground.view.person.main.AlarmItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-10-30.
 */
public class MainAlarmAdapter extends BaseAdapter{

    List<NotiDataResult> items = new ArrayList<NotiDataResult>();

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
        } else {
            view = (AlarmItemView) convertView;
        }
        view.setAlarmItemData(items.get(position));
        return view;
    }

    public void add(NotiDataResult item) {
        items.add(item);
        notifyDataSetChanged();
    }
    int totalCount;
    int page;

    public int getPage(){return page;}
    public void setPgae(int page){this.page = page;}
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalCount() {
        return totalCount;
    }
    public int getNextPage(){
        if(totalCount-items.size()>0){
            page ++;
            return page;
        }
        return -1;
    }
}
