package com.jxd.oa.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.LeaveApplication;
import com.jxd.oa.constants.Const;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：请假单
 * Created by cy on 2014/9/14.
 * *****************************************
 */
public class LeaveApplicationAdapter extends AbstractAdapter<LeaveApplication> {
    public LeaveApplicationAdapter(Context context, List<LeaveApplication> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getInflater().inflate(R.layout.item_leave_application, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title_tv.setText(getItem(position).getLeaveReason());
        viewHolder.date_tv.setText(getItem(position).getStartDate() + "至" + getItem(position).getEndDate());
        if (getItem(position).getAuditStatus() == Const.STATUS_REFUSE.getValue()) {
            viewHolder.status_tv.setTextColor(getContext().getResources().getColor(R.color.color_red));
        } else if (getItem(position).getAuditStatus() == Const.STATUS_REFUSE.getValue()) {
            viewHolder.status_tv.setTextColor(getContext().getResources().getColor(R.color.color_being));
        } else {
            viewHolder.status_tv.setTextColor(getContext().getResources().getColor(R.color.color_black_font));
        }
        viewHolder.status_tv.setText(Const.getName("STATUS_", getItem(position).getAuditStatus()));
        return view;
    }

    static class ViewHolder {
        @ViewInject(R.id.title_tv)
        private TextView title_tv;
        @ViewInject(R.id.date_tv)
        private TextView date_tv;
        @ViewInject(R.id.status_tv)
        private TextView status_tv;
    }
}
