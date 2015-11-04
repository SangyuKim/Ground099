package com.android.ground.ground.controller.fc.fcmain;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.fc.fcmain.FCMemberListItem;
import com.android.ground.ground.model.naver.MovieItem;
import com.android.ground.ground.model.naver.MovieItemView;
import com.android.ground.ground.view.OnAdapterProfileListener;
import com.android.ground.ground.view.OnAdapterReplyListener;
import com.android.ground.ground.view.OnProfileClickListener;
import com.android.ground.ground.view.OnReplyClickListener;
import com.android.ground.ground.view.fc.fcmain.FCMemberItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class FCMemberAdapter extends BaseAdapter
        implements OnReplyClickListener, OnProfileClickListener {

    List<FCMemberListItem> items = new ArrayList<FCMemberListItem>();

//    String keyword;
//    int totalCount;
//
//    public void setKeyword(String keyword) {
//        this.keyword = keyword;
//    }
//    public String getKeyword() {
//        return keyword;
//    }
//
//    public void setTotalCount(int totalCount) {
//        this.totalCount = totalCount;
//    }
//    public int getTotalCount() {
//        return totalCount;
//    }
//
//    public int getStartIndex() {
//        if (items.size() < totalCount) {
//            return items.size() + 1;
//        }
//        return -1;
//    }

    public void add(FCMemberListItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
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
        FCMemberItemView view;
        if (convertView == null) {
            view = new FCMemberItemView(parent.getContext());
            view.setOnProfileListener(this);
            view.setOnReplyListener(this);
        } else {
            view = (FCMemberItemView)convertView;
        }
        view.setFCMemberListItem(items.get(position));
        return view;
    }

    OnAdapterProfileListener mProfileListener;
    public void setOnAdapterProfileListener(OnAdapterProfileListener listener) {
        mProfileListener = listener;
    }
    @Override
    public void onProfileClick(View view, Profile data) {
        if (mProfileListener != null) {
            mProfileListener.onAdapterProfileClick(this, view, data);
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


}
