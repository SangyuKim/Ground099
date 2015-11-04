package com.android.ground.ground.controller.person.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.android.ground.ground.R;
import com.android.ground.ground.model.MyApplication;
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.naver.MovieItemView;
import com.android.ground.ground.model.person.message.MyMessageItem;
import com.android.ground.ground.view.OnNoClickListener;
import com.android.ground.ground.view.OnProfileClickListener;
import com.android.ground.ground.view.OnReplyClickListener;
import com.android.ground.ground.view.OnYesClickListener;
import com.android.ground.ground.view.person.main.SearchPlayerTestItemView;
import com.android.ground.ground.view.person.message.MyMessageItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class MyMessageAdapter extends BaseAdapter implements OnProfileClickListener,OnReplyClickListener, OnNoClickListener, OnYesClickListener {
    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    List<MyMessageItem> items = new ArrayList<MyMessageItem>();
    private boolean[] isCheckedConfrim;
    int isVisible = View.GONE;

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


        if (convertView == null) {

            viewHolder = new ViewHolder();
            MyMessageItemView view = new MyMessageItemView(parent.getContext());
            convertView = view;
            view.setOnProfileListener(this);
            view.setOnReplyListener(this);
            view.setOnYesListener(this);
            view.setOnNoListener(this);
            viewHolder.chk = (CheckBox) convertView.findViewById(R.id.checkBox_message);
            convertView.setTag(viewHolder);

        } else {
//            view = (MyMessageItemView) convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.chk.setFocusable(false);
        viewHolder.chk.setClickable(false);
        viewHolder.chk.setChecked(isCheckedConfrim[position]);
        viewHolder.chk.setVisibility(isVisible);
        return convertView;
    }
    class ViewHolder {
        public CheckBox chk = null;
    }
    public interface OnAdapterProfileListener {
        public void onAdapterProfileClick(MyMessageAdapter adapter, MyMessageItemView view, Profile data);
    }
    OnAdapterProfileListener mProfileListener;
    public void setOnAdapterProfileListener(OnAdapterProfileListener listener) {
        mProfileListener = listener;
    }
    @Override
    public void onProfileClick(MyMessageItemView view, Profile data) {
        if (mProfileListener != null) {
            mProfileListener.onAdapterProfileClick(this, view, data);
        }
    }
    public interface OnAdapterNoListener {
        public void onAdapterNoClick(MyMessageAdapter adapter, MyMessageItemView view);
    }
    OnAdapterNoListener mNoListener;
    public void setOnAdapterNoListener(OnAdapterNoListener listener) {
        mNoListener = listener;
    }
    @Override
    public void onNoClick(MyMessageItemView view) {
        if (mNoListener != null) {
            mNoListener.onAdapterNoClick(this, view);
        }
    }
    public interface OnAdapterReplyListener {
        public void onAdapterReplyClick(MyMessageAdapter adapter, MyMessageItemView view);
    }
    OnAdapterReplyListener mReplyListener;
    public void setOnAdapterReplyListener(OnAdapterReplyListener listener) {
        mReplyListener = listener;
    }
    @Override
    public void onReplyClick(MyMessageItemView view) {
        if (mReplyListener != null) {
            mReplyListener.onAdapterReplyClick(this, view);
        }
    }
    public interface OnAdapterYesListener {
        public void onAdapterYesClick(MyMessageAdapter adapter, MyMessageItemView view);
    }
    OnAdapterYesListener mYesListener;
    public void setOnAdapterYesListener(OnAdapterYesListener listener) {
        mYesListener = listener;
    }
    @Override
    public void onYesClick(MyMessageItemView view) {
        if (mYesListener != null) {
            mYesListener.onAdapterYesClick(this, view);
        }
    }
}
