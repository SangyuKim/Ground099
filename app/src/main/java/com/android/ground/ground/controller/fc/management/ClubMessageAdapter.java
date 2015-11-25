package com.android.ground.ground.controller.fc.management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.android.ground.ground.model.message.ClubMessageDataResult;
import com.android.ground.ground.model.message.MyMessageDataResult;
import com.android.ground.ground.view.OnAdapterNoListener;
import com.android.ground.ground.view.OnAdapterProfileListener;
import com.android.ground.ground.view.OnAdapterReplyListener;
import com.android.ground.ground.view.OnAdapterYesListener;
import com.android.ground.ground.view.OnNoClickListener;
import com.android.ground.ground.view.OnProfileClickListener;
import com.android.ground.ground.view.OnReplyClickListener;
import com.android.ground.ground.view.OnYesClickListener;
import com.android.ground.ground.view.person.message.MyMessageItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class ClubMessageAdapter extends BaseAdapter implements OnProfileClickListener,OnReplyClickListener, OnNoClickListener, OnYesClickListener {
    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    List<ClubMessageDataResult> items = new ArrayList<ClubMessageDataResult>();
    private boolean[] isCheckedConfrim;
    int isVisible = View.GONE;

    public ClubMessageAdapter() {
    }

    public ClubMessageAdapter(Context c, List<ClubMessageDataResult> items){
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
//        if (convertView == null) {
//
//            viewHolder = new ViewHolder();
//            MyMessageItemView view = new MyMessageItemView(parent.getContext());
//            convertView = view;
//            viewHolder.chk = (CheckBox) convertView.findViewById(R.id.checkBox_message);
//            convertView.setTag(viewHolder);
//
//        } else {
////            view = (MyMessageItemView) convertView;
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
////        viewHolder.chk.setFocusable(false);
////        viewHolder.chk.setClickable(false);
////        viewHolder.chk.setChecked(isCheckedConfrim[position]);
////        viewHolder.chk.setVisibility(isVisible);
//        return convertView;
        MyMessageItemView view;
        if (convertView == null) {
            view = new MyMessageItemView(parent.getContext());
            view.setOnProfileListener(this);
            view.setOnReplyListener(this);
            view.setOnYesListener(this);
            view.setOnNoListener(this);
        } else {
            view = (MyMessageItemView) convertView;
        }
        view.setMyMessageItem(items.get(position));
        return view;
    }
    class ViewHolder {
        public CheckBox chk = null;
    }

    OnAdapterProfileListener mProfileListener;
    public void setOnAdapterProfileListener(OnAdapterProfileListener listener) {
        mProfileListener = listener;
    }
    @Override
    public void onProfileClick(View view) {
        if (mProfileListener != null) {
            mProfileListener.onAdapterProfileClick(this, view);
        }
    }
    OnAdapterNoListener mNoListener;
    public void setOnAdapterNoListener(OnAdapterNoListener listener) {
        mNoListener = listener;
    }
    @Override
    public void onNoClick(View view) {
        if (mNoListener != null) {
            mNoListener.onAdapterNoClick(this, view);
        }
    }
    OnAdapterReplyListener mReplyListener;
    public void setOnAdapterReplyListener(OnAdapterReplyListener listener) {
        mReplyListener = listener;
    }
    @Override
    public void onReplyClick(View view) {
        if (mReplyListener != null) {
            mReplyListener.onAdapterReplyClick(this, view);
        }
    }
    OnAdapterYesListener mYesListener;
    public void setOnAdapterYesListener(OnAdapterYesListener listener) {
        mYesListener = listener;
    }
    @Override
    public void onYesClick(View view) {
        if (mYesListener != null) {
            mYesListener.onAdapterYesClick(this, view);
        }
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

    public void add(ClubMessageDataResult item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

}
