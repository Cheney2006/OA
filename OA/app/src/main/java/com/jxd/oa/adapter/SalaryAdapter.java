package com.jxd.oa.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.Salary;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：员工工资
 * Created by cy on 2014/9/14.
 * *****************************************
 */
public class SalaryAdapter extends AbstractAdapter<Salary> {

    public SalaryAdapter(Context context, List<Salary> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getInflater().inflate(R.layout.item_task, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.yearMonth_tv.setText(getItem(position).getYearMonth());
        viewHolder.total_tv.setText(getItem(position).getActualSalary() + "");
        return view;
    }

    static class ViewHolder {
        @ViewInject(R.id.yearMonth_tv)
        private TextView yearMonth_tv;
        @ViewInject(R.id.total_tv)
        private TextView total_tv;
    }
}
