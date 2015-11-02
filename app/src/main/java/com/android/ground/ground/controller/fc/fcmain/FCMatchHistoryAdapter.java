package com.android.ground.ground.controller.fc.fcmain;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;

import com.android.ground.ground.model.fc.fcmain.FCMatchGroupItem;
import com.android.ground.ground.model.fc.fcmain.FCMatchHistoryListItem;
import com.android.ground.ground.model.fc.fcmain.FCMemberListItem;
import com.android.ground.ground.view.fc.fcmain.FCMatchGroupItemView;
import com.android.ground.ground.view.fc.fcmain.FCMatchHistoryItemView;
import com.android.ground.ground.view.fc.fcmain.FCMemberItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class FCMatchHistoryAdapter extends BaseExpandableListAdapter {
    List<FCMatchGroupItem> items = new ArrayList<FCMatchGroupItem>();

    public void add(String groupName, List<FCMatchHistoryListItem> child) {
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
            FCMatchHistoryListItem pItem;
            for(int i=0; i<child.size(); i++) {

                pItem = new FCMatchHistoryListItem();
                g.children.add(pItem);
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
        }
        v.setFCMatchHistoryListItem(items.get(groupPosition).children.get(childPosition));

        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
