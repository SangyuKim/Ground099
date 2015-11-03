package com.android.ground.ground.controller.person.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.naver.MovieItemView;
import com.android.ground.ground.model.person.message.MyMessageItem;
import com.android.ground.ground.view.person.message.MyMessageItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class MyMessageAdapter extends BaseAdapter {
    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    List<MyMessageItem> items = new ArrayList<MyMessageItem>();
    private boolean[] isCheckedConfrim;

    public MyMessageAdapter(Context c, List<MyMessageItem> items){
        inflater = LayoutInflater.from(c);
        this.items =items;
        this.isCheckedConfrim = new boolean[this.items.size()];

    }
    public void setAllChecked(boolean ischeked) {
        int tempSize = isCheckedConfrim.length;
        for(int a=0 ; a<tempSize ; a++){
            isCheckedConfrim[a] = ischeked;
        }
    }
    public void setChecked(int position) {
        isCheckedConfrim[position] = !isCheckedConfrim[position];
    }
    public ArrayList<Integer> getChecked(){
        int tempSize = isCheckedConfrim.length;
        ArrayList<Integer> mArrayList = new ArrayList<Integer>();
        for(int b=0 ; b<tempSize ; b++){
            if(isCheckedConfrim[b]){
                mArrayList.add(b);
            }
        }
        return mArrayList;
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
//        MyMessageItemView view;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_controller_person_message1, null);
            viewHolder.chk = (CheckBox) convertView.findViewById(R.id.checkBox_message);
            convertView.setTag(viewHolder);
            Log.d("hello", "convertView null");

        } else {
//            view = (MyMessageItemView) convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.chk.setFocusable(false);
        viewHolder.chk.setClickable(false);
        viewHolder.chk.setChecked(isCheckedConfrim[position]);
        return convertView;
    }
    class ViewHolder {
        public CheckBox chk = null;
    }
}
