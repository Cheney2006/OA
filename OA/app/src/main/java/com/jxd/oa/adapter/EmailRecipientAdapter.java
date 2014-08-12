package com.jxd.oa.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.Email;
import com.jxd.oa.bean.EmailRecipient;
import com.jxd.oa.constants.SysConfig;
import com.yftools.ViewUtil;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：邮件接收人
 * Created by cy on 2014/8/6.
 * *****************************************
 */
public class EmailRecipientAdapter extends AbstractAdapter<EmailRecipient> {

    public EmailRecipientAdapter(Context context, List<EmailRecipient> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getInflater().inflate(R.layout.item_email_recipient, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (getItem(position).getToUser() != null) {
            viewHolder.name_tv.setText(getItem(position).getToUser().getName());
        }
        if (getItem(position).getReadTime() != null) {
            viewHolder.readTime_tv.setText(getItem(position).getReadTime());
        }
        return view;
    }

    static class ViewHolder {
        @ViewInject(R.id.name_tv)
        private TextView name_tv;
        @ViewInject(R.id.readTime_tv)
        private TextView readTime_tv;
    }
}
