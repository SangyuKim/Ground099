package com.android.ground.ground.model.person.main.matchinfo;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;

import com.android.ground.ground.view.person.main.SearchMatchGroupItemView;
import com.android.ground.ground.view.person.main.SearchMatchItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongja94 on 2015-10-19.
 */
public class MatchInfoAdapter extends BaseExpandableListAdapter implements SearchMatchItemView.OnExtraButtonClickListener {
    List<CheckMatchListGroupItem> items = new ArrayList<CheckMatchListGroupItem>();



    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }
    String keyword;
    int totalCount;
    int page;

    String filter;

    public void setFilter(String filter){this.filter =filter;}
    public String getFilter(){return filter;}
    public int getPage(){return page;}
    public void setPage(int page){this.page = page;}
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
        int totalChildrenCount =0;
        for(int i=0; i<items.size(); i++){
            totalChildrenCount += items.get(i).children.size();
        }
        if(totalCount-totalChildrenCount>0){
            page ++;
            return page;
        }
        return -1;
    }
    int futurePage;
    int ingPage;
    int endPage;
    int totalFuturePage;

    public int getTotalFuturePage() {
        return totalFuturePage;
    }

    public void setTotalFuturePage(int totalFuturePage) {
        this.totalFuturePage = totalFuturePage;
    }

    public int getTotalIngPage() {
        return totalIngPage;
    }

    public void setTotalIngPage(int totalIngPage) {
        this.totalIngPage = totalIngPage;
    }

    public int getTotalEndPage() {
        return totalEndPage;
    }

    public void setTotalEndPage(int totalEndPage) {
        this.totalEndPage = totalEndPage;
    }

    int totalIngPage;
    int totalEndPage;
    public int getFuturePage(){
        return futurePage;
    }
    public void setFuturePage(int page){
        futurePage =page;
    }
    public int getIngPage(){
        return ingPage;
    }
    public void setIngPage(int page){
        ingPage = page;
    }
    public int getEndPage(){
        return endPage;
    }
    public void setEndPage(int page){
        endPage=page;
    }

    public void add(String groupName, List<MatchInfoResult> child) {
        CheckMatchListGroupItem g = null;
        for (CheckMatchListGroupItem item : items) {
            if (item.text.equals(groupName)) {
                g = item;
                break;
            }
        }
        if (g == null) {
            g = new CheckMatchListGroupItem();
             g.text = groupName;
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
        SearchMatchGroupItemView  v;
        if (convertView != null) {
            v = (SearchMatchGroupItemView )convertView;
        } else {
            v = new SearchMatchGroupItemView (parent.getContext());
        }
        v.setGroupItem(items.get(groupPosition));
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        SearchMatchItemView view;
        if (convertView != null) {
            view = (SearchMatchItemView)convertView;
        } else {
            view = new SearchMatchItemView(parent.getContext());
            view.setOnExtraButtonClickListner(this);
        }

        view.setMatchInfoResult(items.get(groupPosition).children.get(childPosition));
        view.setInvisible();
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public interface OnAdapterExtraButtonClickListener{
        void setOnExtraButtonClick(ExpandableListAdapter adapter, View view);
    }
    OnAdapterExtraButtonClickListener mListener;
    public void setOnAdapterExtraButtonClickListener(OnAdapterExtraButtonClickListener listener){
        mListener = listener;
    }
    @Override
    public void setOnButtonClick(View view) {
        if(mListener != null){
            mListener.setOnExtraButtonClick(this, view);
        }
    }
}
