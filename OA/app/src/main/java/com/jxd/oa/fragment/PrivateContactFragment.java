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
import com.jxd.oa.activity.ContactDetailActivity;
import com.jxd.oa.adapter.ContactAdapter;
import com.jxd.oa.adapter.ContactExpandableAdapter;
import com.jxd.oa.bean.Contact;
import com.jxd.oa.bean.ContactCategory;
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
public class PrivateContactFragment extends AbstractFragment {

    @ViewInject(R.id.mListView)
    private ListView mListView;
    @ViewInject(R.id.mExpandableListView)
    private ExpandableListView mExpandableListView;
    private List<Contact> contactList;
    private ContactAdapter adapter;
    private List<ContactCategory> mGroupList;
    private List<List<Contact>> mChildList;
    private ContactExpandableAdapter expandableAdapter;
    private Status status = Status.ALL;

    private enum Status {
        ALL, GROUP;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_contact, container, false);
        ViewUtil.inject(this, convertView);
        fillList();
        return convertView;
    }

    private void startActivity(Contact contact) {
        Intent intent = new Intent(mContext, ContactDetailActivity.class);
        intent.putExtra("contact", contact);
        startActivity(intent);
    }

    public void fillList() {
        status = Status.ALL;
        mListView.setVisibility(View.VISIBLE);
        mExpandableListView.setVisibility(View.GONE);
        try {
            contactList = DbOperationManager.getInstance().getBeans(Selector.from(Contact.class).where("userId", "=", SysConfig.getInstance().getUserId()));
        } catch (DbException e) {
            LogUtil.e(e);
        }
        if (adapter == null) {
            adapter = new ContactAdapter(mContext, contactList);
            mListView.setAdapter(adapter);
        } else {
            adapter.setDataList(contactList);
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
            mGroupList = DbOperationManager.getInstance().getBeans(Selector.from(ContactCategory.class).where("userId", "=", SysConfig.getInstance().getUserId()));
            if (mGroupList != null) {
                mChildList = new ArrayList<List<Contact>>();
                for (ContactCategory contactCategory : mGroupList) {
                    List<Contact> childList = DbOperationManager.getInstance().getBeans(Selector.from(Contact.class).where("userId", "=", SysConfig.getInstance().getUserId()).and("categoryId", "=", contactCategory.getId()));
                    mChildList.add(childList);
                }
            }
            if (expandableAdapter == null) {
                expandableAdapter = new ContactExpandableAdapter(mContext, mGroupList, mChildList);
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
