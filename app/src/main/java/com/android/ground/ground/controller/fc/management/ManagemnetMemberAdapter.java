package com.android.ground.ground.controller.fc.management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.android.ground.ground.R;
import com.android.ground.ground.model.Profile;
import com.android.ground.ground.model.fc.management.ManagementMemberItem;
import com.android.ground.ground.model.person.message.MyMessageItem;
import com.android.ground.ground.view.OnAdapterProfileListener;
import com.android.ground.ground.view.OnAdapterReplyListener;
import com.android.ground.ground.view.OnProfileClickListener;
import com.android.ground.ground.view.OnReplyClickListener;
import com.android.ground.ground.view.fc.management.ManagementMemberItemView;
import com.android.ground.ground.view.person.message.MyMessageItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tacademy on 2015-11-02.
 */
public class ManagemnetMemberAdapter extends BaseAdapter {
    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    List<ManagementMemberItem> items = new ArrayList<ManagementMemberItem>();
    private boolean[] isCheckedConfrim;

    public ManagemnetMemberAdapter(Context c, List<ManagementMemberItem> items){
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
            ManagementMemberItemView view = new ManagementMemberItemView(parent.getContext());
            convertView = view;
            viewHolder.chk = (CheckBox) convertView.findViewById(R.id.checkBox15);
            convertView.setTag(viewHolder);

        } else {
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
