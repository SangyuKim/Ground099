package com.android.ground.ground.model.person.main.searchClub;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.ground.ground.view.OnAdapterDialogListener;
import com.android.ground.ground.view.OnAdapterSpecificDialogListener;
import com.android.ground.ground.view.OnDialogClickListener;
import com.android.ground.ground.view.OnSpecificDialogClickListener;
import com.android.ground.ground.view.person.main.SearchFCItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class SearchClubAdapter extends BaseAdapter implements OnSpecificDialogClickListener {

    List<SearchClubResult> items = new ArrayList<SearchClubResult>();

    String keyword;
    int totalCount;
    int page;

    String filter;

    public void setFilter(String filter){this.filter =filter;}
    public String getFilter(){return filter;}
    public int getPage(){return page;}
    public void setPgae(int page){this.page = page;}
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public String getKeyword() {
        return keyword;
    }
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

    public void add(SearchClubResult item) {
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
        SearchFCItemView view;
        if (convertView == null) {
            view = new SearchFCItemView(parent.getContext());
            view.setOnSpecificDialogListener(this);
        } else {
            view = (SearchFCItemView) convertView;
        }
        view.setSearchClubResult(items.get(position));
        return view;
    }

    OnAdapterSpecificDialogListener mListener;
    public void setOnAdapterSpecificDialogListener(OnAdapterSpecificDialogListener listener){
        mListener = listener;
    }
    @Override
    public void onDialogClick(View view, String tag) {
        if(mListener != null){
            mListener.onAdapterDialogClick(this, view, tag);
        }
    }
}
