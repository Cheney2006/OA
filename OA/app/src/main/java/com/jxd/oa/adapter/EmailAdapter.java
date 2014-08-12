package com.jxd.oa.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.Email;
import com.jxd.oa.bean.User;
import com.jxd.oa.constants.Const;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.DbException;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：邮件
 * Created by cy on 2014/8/6.
 * *****************************************
 */
public class EmailAdapter extends AbstractAdapter<Email> {
    public EmailAdapter(Context context, List<Email> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getInflater().inflate(R.layout.item_email, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title_tv.setText(getItem(position).getTitle());
        if (TextUtils.isEmpty(getItem(position).getAttachmentName())) {
            viewHolder.title_tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            viewHolder.title_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_attach, 0, 0, 0);
        }
        if (getItem(position).getSendTime() != null) {
            viewHolder.date_tv.setText(DateUtil.dateTimeToString(getItem(position).getSendTime()));
        }
        if (!getItem(position).getFromId().equals(SysConfig.getInstance().getUserId())) {//收件箱时
            if (getItem(position).getFromUser() != null) {
                viewHolder.send_tv.setText(getItem(position).getFromUser().getName());
            }
        } else {
            viewHolder.send_tv.setText("");
        }
        viewHolder.important_tv.setText(Const.getName("TYPE_IMPORTANT_", getItem(position).getImportant()));
        return view;
    }

    static class ViewHolder {
        @ViewInject(R.id.title_tv)
        private TextView title_tv;
        @ViewInject(R.id.send_tv)
        private TextView send_tv;
        @ViewInject(R.id.date_tv)
        private TextView date_tv;
        @ViewInject(R.id.important_tv)
        private TextView important_tv;
    }
}
