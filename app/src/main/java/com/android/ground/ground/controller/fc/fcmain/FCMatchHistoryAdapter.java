package com.android.ground.ground.controller.fc.fcmain;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.android.ground.ground.model.fc.fcmain.ClubMatchList.ClubMatchListResult;
import com.android.ground.ground.model.fc.fcmain.FCMatchGroupItem;
import com.android.ground.ground.view.OnDialogClickListener;
import com.android.ground.ground.view.OnExpandableAdapterDialogListener;
import com.android.ground.ground.view.fc.fcmain.FCMatchGroupItemView;
import com.android.ground.ground.view.fc.fcmain.FCMatchHistoryItemView;

import java.util.ArrayList;
import java.util.List;

public class FCMatchHistoryAdapter extends BaseExpandableListAdapter implements OnDialogClickListener {
    List<FCMatchGroupItem> items = new ArrayList<FCMatchGroupItem>();


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

    public int getStartIndex() {
        if (items.size() < totalCount) {
            return items.size() + 1;
        }
        return -1;
    }
    public int getNextPage(){
        if(totalCount-items.size()>0){
            page ++;
            return page;
        }
        return -1;
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }



    public void add(String groupName, List<ClubMatchListResult> child) {
        FCMatchGroupItem g = null;
        for (FCMatchGroupItem item : items) {
            if (item.groupName.equals(groupName)) {
                g = item;
                break;
            }
        }
        if (g == null) {
            g = new FCMatchGroupItem();
            g.groupName = groupName;
            items.add(g);
        }

        if (child != null) {
            for(int i=0; i<child.size(); i++) {
                g.children.add(child.get(i));
            }//for
        }//if
        notifyDataSetChanged();
    }


    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return  items.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return(long)groupPosition << 32 | 0xFFFFFFFF;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return  (long)groupPosition << 32 | childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        FCMatchGroupItemView v;
        if (convertView != null) {
            v = (FCMatchGroupItemView)convertView;
        } else {
            v = new FCMatchGroupItemView(parent.getContext());
        }
        v.setGroupItem(items.get(groupPosition));
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        FCMatchHistoryItemView v;
        if (convertView != null) {
            v = (FCMatchHistoryItemView)convertView;
        } else {
            v = new FCMatchHistoryItemView(parent.getContext());
            v.setOnDialogListener(this);
        }
        v.setFCMatchHistoryListItem(items.get(groupPosition).children.get(childPosition));

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    OnExpandableAdapterDialogListener mDialogListener;
    public void setOnExpandableAdapterProfileListener(OnExpandableAdapterDialogListener listener) {
        mDialogListener = listener;
    }

    @Override
    public void onDialogClick(View view) {
        if(mDialogListener != null){
            mDialogListener.onAdapterDialogClick(this, view);
        }
    }
}
