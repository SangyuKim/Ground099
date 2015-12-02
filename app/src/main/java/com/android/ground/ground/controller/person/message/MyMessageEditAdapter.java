package com.android.ground.ground.controller.person.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.android.ground.ground.model.message.MyMessageDataResult;
import com.android.ground.ground.view.person.message.MyMessageItemViewEdit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class MyMessageEditAdapter extends BaseAdapter{
    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    List<MyMessageDataResult> items = new ArrayList<MyMessageDataResult>();
    private boolean[] isCheckedConfrim;
    int isVisible = View.GONE;


    public void setAllChecked(boolean ischeked) {
        int tempSize = isCheckedConfrim.length;
        for(int a=0 ; a<tempSize ; a++){
            isCheckedConfrim[a] = ischeked;
        }
    }
    public void setCheckBoxVisible(int isVisible){
        this.isVisible = isVisible;
    }
    public void setChecked(int position) {
        isCheckedConfrim[position] = !isCheckedConfrim[position];
    }
    public boolean getChecked(int position){
        return isCheckedConfrim[position];
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

        MyMessageItemViewEdit view;
        if (convertView == null) {
            view = new MyMessageItemViewEdit(parent.getContext());
        } else {
            view = (MyMessageItemViewEdit) convertView;
        }
        view.setMyMessageItem(items.get(position));
        view.setCheckBox_messageVisible();
        return view;
    }
    class ViewHolder {
        public CheckBox chk = null;
    }


    int totalCount;
    int page;

    public int getPage(){return page;}
    public void setPgae(int page){this.page = page;}
    public void setTotalCount(int totalCount) {
        this.isCheckedConfrim = new boolean[totalCount +1 ];
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

    public void add(MyMessageDataResult item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

}
