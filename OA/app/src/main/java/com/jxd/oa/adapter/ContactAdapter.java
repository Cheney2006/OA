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
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.Contact;
import com.jxd.oa.constants.Constant;
import com.yftools.ViewUtil;
import com.yftools.util.AndroidUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：通讯录（公共、个人）
 * Created by cy on 2014/8/8.
 * *****************************************
 */
public class ContactAdapter extends AbstractAdapter<Contact> {

    public ContactAdapter(Context context, List<Contact> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        Contact data = getItem(position);
        if (view == null) {
            viewHolder = new ViewHolder(data);
            view = getInflater().inflate(R.layout.item_contact, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.update(data);
        }
        viewHolder.name_tv.setText(data.getName());
        viewHolder.sex_tv.setText(data.getSex());
        viewHolder.companyName_tv.setText(data.getCompanyName());
        return view;
    }

    private class ViewHolder {
        private Contact data;

        public ViewHolder(Contact data) {
            this.data = data;
        }

        public void update(Contact data) {
            this.data = data;
        }

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
