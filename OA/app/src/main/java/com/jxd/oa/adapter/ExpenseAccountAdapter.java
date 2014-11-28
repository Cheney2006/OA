package com.jxd.oa.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.ExpenseAccount;
import com.jxd.oa.constants.Const;
import com.yftools.ViewUtil;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：报销单
 * Created by cy on 2014/10/24.
 * *****************************************
 */
public class ExpenseAccountAdapter extends AbstractAdapter<ExpenseAccount> {
    public ExpenseAccountAdapter(Context context, List<ExpenseAccount> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getInflater().inflate(R.layout.item_expense_account, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title_tv.setText(getItem(position).getItemName());
        viewHolder.date_tv.setText(DateUtil.dateToString(getItem(position).getApplyDate()));
        viewHolder.money_tv.setText("￥" + getItem(position).getMoney());
        if (getItem(position).getAuditStatus() == (Integer)Const.STATUS_AUDIT_REFUSE.getValue()) {
            viewHolder.status_tv.setTextColor(getContext().getResources().getColor(R.color.color_red));
        } else if (getItem(position).getAuditStatus() == (Integer)Const.STATUS_AUDIT_PASS.getValue()) {
            viewHolder.status_tv.setTextColor(getContext().getResources().getColor(R.color.color_being));
        } else {
            viewHolder.status_tv.setTextColor(getContext().getResources().getColor(R.color.color_black_font));
        }
        viewHolder.status_tv.setText(Const.getName("STATUS_AUDIT_", getItem(position).getAuditStatus()));
        return view;
    }

    static class ViewHolder {
        @ViewInject(R.id.title_tv)
        private TextView title_tv;
        @ViewInject(R.id.date_tv)
        private TextView date_tv;
        @ViewInject(R.id.money_tv)
        private TextView money_tv;
        @ViewInject(R.id.status_tv)
        private TextView status_tv;
    }
}
