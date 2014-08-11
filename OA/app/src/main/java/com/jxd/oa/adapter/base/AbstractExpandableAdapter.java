package com.jxd.oa.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * *****************************************
 * Description ：基类baseAdapter
 * Created by cy on 2014/8/8.
 * *****************************************
 */
public abstract class AbstractExpandableAdapter<T,C> extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<T> groupList;
    private List<List<C>> childListList;

    protected AbstractExpandableAdapter(Context context, List<T> groupList, List<List<C>> childListList) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        this.groupList = groupList;
        this.childListList = childListList;
    }


    /**
     *   Description:取得分组数
     */
    @Override
    public int getGroupCount() {
        return groupList != null ? groupList.size() : 0;
    }

    /**
     *   Description:取得子列表个数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return childListList != null ? childListList.get(groupPosition).size() : 0;
    }

    /**
     *   Description:取得组对象
     */
    @Override
    public T getGroup(int groupPosition) {
        return groupList != null ? groupList.get(groupPosition) : null;
    }

    /**
     *   Description:
     */
    @Override
    public C getChild(int groupPosition, int childPosition) {
        return childListList != null ? childListList.get(groupPosition).get(childPosition) : null;
    }

    /**
     *   Description:取得组id
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     *   Description:取得子id
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     *   Description:
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     *   Description:生成组内容
     */
    @Override
    public abstract View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

    /**
     *   Description:生成子内容
     */
    @Override
    public abstract View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public List<T> getGroupList() {
        return groupList;
    }

    public List<List<C>> getChildListList() {
        return childListList;
    }

    public void setGroupList(List<T> groupList) {
        this.groupList = groupList;
    }

    public void setChildList(List<List<C>> childListList) {
        this.childListList = childListList;
    }

    public Context getContext() {
        return this.mContext;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }
}
