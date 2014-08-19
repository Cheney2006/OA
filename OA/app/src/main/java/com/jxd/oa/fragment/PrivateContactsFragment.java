package com.jxd.oa.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.jxd.oa.R;
import com.jxd.oa.activity.ContactsDetailActivity;
import com.jxd.oa.adapter.ContactsAdapter;
import com.jxd.oa.adapter.ContactsExpandableAdapter;
import com.jxd.oa.bean.Contacts;
import com.jxd.oa.bean.ContactsCategory;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.fragment.base.AbstractFragment;
import com.jxd.oa.utils.DbOperationManager;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnChildClick;
import com.yftools.view.annotation.event.OnItemClick;

import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：私人通讯录
 * Created by cy on 2014/8/14.
 * *****************************************
 */
public class PrivateContactsFragment extends AbstractFragment {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    @ViewInject(R.id.mExpandableListView)
    private ExpandableListView mExpandableListView;
    private List<Contacts> contactsList;
    private ContactsAdapter adapter;
    private List<ContactsCategory> mGroupList;
    private List<List<Contacts>> mChildList;
    private ContactsExpandableAdapter expandableAdapter;
    private Status status = Status.ALL;

    private enum Status {
        ALL, GROUP;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ViewUtil.inject(this, convertView);
        fillList();
        return convertView;
    }

    private void startActivity(Contacts contacts) {
        Intent intent = new Intent(mContext, ContactsDetailActivity.class);
        intent.putExtra("contacts", contacts);
        startActivity(intent);
    }

    public void fillList() {
        status = Status.ALL;
        mListView.setVisibility(View.VISIBLE);
        mExpandableListView.setVisibility(View.GONE);
        try {
            contactsList = DbOperationManager.getInstance().getBeans(Selector.from(Contacts.class).where("userId", "=", SysConfig.getInstance().getUserId()));
        } catch (DbException e) {
            LogUtil.e(e);
        }
        if (adapter == null) {
            adapter = new ContactsAdapter(mContext, contactsList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(contactsList);
            adapter.notifyDataSetChanged();
        }
    }

    @OnItemClick(R.id.mListView)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(adapter.getItem(position));
    }

    @OnChildClick(R.id.mExpandableListView)
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        startActivity(expandableAdapter.getChild(groupPosition, childPosition));
        return false;
    }

    public void fillExpandableList() {
        status = Status.GROUP;
        mListView.setVisibility(View.GONE);
        mExpandableListView.setVisibility(View.VISIBLE);
        try {
            mGroupList = DbOperationManager.getInstance().getBeans(Selector.from(ContactsCategory.class).where("userId", "=", SysConfig.getInstance().getUserId()));
            if (mGroupList != null) {
                mChildList = new ArrayList<List<Contacts>>();
                for (ContactsCategory contactsCategory : mGroupList) {
                    List<Contacts> childList = DbOperationManager.getInstance().getBeans(Selector.from(Contacts.class).where("userId", "=", SysConfig.getInstance().getUserId()).and("categoryId", "=", contactsCategory.getId()));
                    mChildList.add(childList);
                }
            }
            if (expandableAdapter == null) {
                expandableAdapter = new ContactsExpandableAdapter(mContext, mGroupList, mChildList);
                mExpandableListView.setAdapter(expandableAdapter);
            } else {
                expandableAdapter.setGroupList(mGroupList);
                expandableAdapter.setChildList(mChildList);
                expandableAdapter.notifyDataSetChanged();
            }
        } catch (DbException e) {
            LogUtil.e(e);
        }
    }

    @Override
    protected void refreshData() {
        if (status == Status.ALL) {
            fillList();
        } else if (status == Status.GROUP) {
            fillExpandableList();
        }
    }
}
