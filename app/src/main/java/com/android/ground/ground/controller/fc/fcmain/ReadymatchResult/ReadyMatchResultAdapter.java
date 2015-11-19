package com.android.ground.ground.controller.fc.fcmain.ReadymatchResult;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.fc.fcmain.ClubAndMember.ClubAndMemberResult;
import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualRes;
import com.android.ground.ground.model.lineup.virtual.res.LineupVirtualResResult;
import com.android.ground.ground.view.OnAdapterCustomTouchListener;
import com.android.ground.ground.view.OnAdapterProfileListener;
import com.android.ground.ground.view.OnAdapterReplyListener;
import com.android.ground.ground.view.OnAdapterSpecificDialogListener;
import com.android.ground.ground.view.OnCustomTouchListener;
import com.android.ground.ground.view.OnProfileClickListener;
import com.android.ground.ground.view.OnReplyClickListener;
import com.android.ground.ground.view.fc.fcmain.FCMemberItemView;
import com.android.ground.ground.view.fc.fcmain.ReadyMatchResultListItemView;

import java.util.ArrayList;
import java.util.List;


public class ReadyMatchResultAdapter extends BaseAdapter implements OnCustomTouchListener{

    List<LineupVirtualResResult> items = new ArrayList<LineupVirtualResResult>();

    public void add(LineupVirtualResResult item){
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
        ReadyMatchResultListItemView view;
        if (convertView == null) {
            view = new ReadyMatchResultListItemView(parent.getContext());
            view.setOnCustomTouchListener(this);
        } else {
            view = (ReadyMatchResultListItemView)convertView;
        }

        view.setReadyMatchResultListItem(items.get(position));
        return view;
    }

    OnAdapterCustomTouchListener mListener;
    public void setOnAdapterCustomTouchListener(OnAdapterCustomTouchListener listener){
        mListener = listener;
    }


    @Override
    public void onTouch(View view, LineupVirtualResResult mItem) {
        if(mListener != null){
            mListener.onTouch(view, mItem);
        }
    }
}
