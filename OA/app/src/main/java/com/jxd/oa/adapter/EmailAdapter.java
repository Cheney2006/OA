package com.jxd.oa.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.Email;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：
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
