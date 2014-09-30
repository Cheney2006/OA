package com.jxd.oa.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.Message;
import com.yftools.ViewUtil;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：消息中心
 * Created by cy on 2014/8/6.
 * *****************************************
 */
public class MessageAdapter extends AbstractAdapter<Message> {

    public MessageAdapter(Context context, List<Message> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getInflater().inflate(R.layout.item_message, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title_tv.setText(getItem(position).getTitle());

        if (getItem(position).getCreatedDate() != null) {
            viewHolder.date_tv.setText(DateUtil.dateTimeToString(getItem(position).getCreatedDate()));
        }
        if (getItem(position).isRead()) {
            viewHolder.title_tv.setTextColor(getContext().getResources().getColor(R.color.color_gray_font));
        } else {
            viewHolder.title_tv.setTextColor(getContext().getResources().getColor(R.color.color_black_font));
        }
        return view;
    }

    static class ViewHolder {
        @ViewInject(R.id.title_tv)
        private TextView title_tv;
        @ViewInject(R.id.date_tv)
        private TextView date_tv;
    }
}
