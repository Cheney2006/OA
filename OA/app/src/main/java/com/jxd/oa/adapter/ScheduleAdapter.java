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
import com.jxd.oa.bean.Schedule;
import com.jxd.oa.constants.Const;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：日程
 * Created by cy on 2014/8/18.
 * *****************************************
 */
public class ScheduleAdapter extends AbstractAdapter<Schedule> {

    public ScheduleAdapter(Context context, List<Schedule> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getInflater().inflate(R.layout.item_schedule, null);
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
        //显示开始时间
        if (getItem(position).getStartData() != null) {
            viewHolder.date_tv.setText(DateUtil.dateToString("HH;mm", getItem(position).getStartData()));
        }
        viewHolder.address_tv.setText(getItem(position).getAddress());
        viewHolder.finished_tv.setText(getItem(position).isFinished() ? "已完成" : "");
        return view;
    }

    static class ViewHolder {
        @ViewInject(R.id.title_tv)
        private TextView title_tv;
        @ViewInject(R.id.address_tv)
        private TextView address_tv;
        @ViewInject(R.id.date_tv)
        private TextView date_tv;
        @ViewInject(R.id.finished_tv)
        private TextView finished_tv;
    }
}
