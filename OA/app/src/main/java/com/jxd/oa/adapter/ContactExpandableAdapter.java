package com.jxd.oa.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractExpandableAdapter;
import com.jxd.oa.bean.Contact;
import com.jxd.oa.bean.ContactCategory;
import com.jxd.oa.constants.Constant;
import com.yftools.ViewUtil;
import com.yftools.util.AndroidUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：联系人分组显示
 * Created by cy on 2014/8/8.
 * *****************************************
 */
public class ContactExpandableAdapter extends AbstractExpandableAdapter<ContactCategory, Contact> {

    public ContactExpandableAdapter(Context context, List<ContactCategory> groupList, List<List<Contact>> childListList) {
        super(context, groupList, childListList);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder(null);
            convertView = getInflater().inflate(R.layout.item_contact_group, null);
            ViewUtil.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.categoryName_tv.setText(getGroup(groupPosition).getGroupName() + "（" + getChildListList().get(groupPosition).size() + "）");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Contact data = getChild(groupPosition, childPosition);
        if (convertView == null) {
            viewHolder = new ViewHolder(data);
            convertView = getInflater().inflate(R.layout.item_contact, null);
            ViewUtil.inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.update(data);
        }
        viewHolder.name_tv.setText(data.getName());
        viewHolder.sex_tv.setText(data.getSex());
        viewHolder.companyName_tv.setText(data.getCompanyName());
        return convertView;
    }

    private class ViewHolder {
        private Contact data;

        public ViewHolder(Contact data) {
            this.data = data;
        }

        public void update(Contact data) {
            this.data = data;
        }

        @ViewInject(R.id.categoryName_tv)
        private TextView categoryName_tv;
        @ViewInject(R.id.name_tv)
        private TextView name_tv;
        @ViewInject(R.id.sex_tv)
        private TextView sex_tv;
        @ViewInject(R.id.companyName_tv)
        private TextView companyName_tv;

        @OnClick(R.id.phone_tv)
        public void phoneClick(View view) {
            callPhoneOrSendMsg(Constant.CALL_PHONE);
        }

        private void callPhoneOrSendMsg(final int type) {
            final List<String> phoneList = new ArrayList<String>();
            if (!TextUtils.isEmpty(data.getMobile())) {
                phoneList.add(data.getMobile());
            }
            if (!TextUtils.isEmpty(data.getHomeTel())) {
                phoneList.add(data.getHomeTel());
            }
            if (phoneList.size() > 0) {
                Dialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("请选择号码")
                        .setItems(phoneList.toArray(new String[phoneList.size()]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (type == Constant.CALL_PHONE) {
                                    AndroidUtil.callPhone(getContext(), phoneList.get(which));
                                } else if (type == Constant.SEND_MSG) {
                                    AndroidUtil.sendMessage(getContext(), phoneList.get(which));
                                }
                            }
                        }).create();
                alertDialog.show();
            } else {
                Toast.makeText(getContext(), "暂无联系方式", Toast.LENGTH_SHORT).show();
            }
        }

        @OnClick(R.id.msg_tv)
        public void msgClick(View view) {
            callPhoneOrSendMsg(Constant.SEND_MSG);
        }
    }
}
