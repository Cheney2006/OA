package com.jxd.oa.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.Address;
import com.jxd.oa.bean.Sign;
import com.jxd.oa.constants.Const;
import com.yftools.ViewUtil;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.List;

/**
 * *****************************************
 * Description ：签到
 * Created by cy on 2014/8/13.
 * *****************************************
 */
public class SignAdapter extends AbstractAdapter<Sign> {

    public SignAdapter(Context context, List<Sign> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getInflater().inflate(R.layout.item_sign, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        String previewDate = null, nowDate = DateUtil.dateToString(getItem(position).getSignTime());
        int idx = position - 1;
        if (idx >= 0 && getItem(idx).getSignTime() != null) {
            previewDate = DateUtil.dateToString(getItem(idx).getSignTime());
        }
        viewHolder.signDate_tv.setText(nowDate);
        viewHolder.signDate_tv.setVisibility(nowDate.equals(previewDate) ? View.GONE : View.VISIBLE);
        viewHolder.signDate_line.setVisibility(nowDate.equals(previewDate) ? View.GONE : View.VISIBLE);
        viewHolder.signAddress_tv.setText(getItem(position).getSignAddress());
        Address vicinityAddress = getItem(position).getVicinityAddress();
        viewHolder.vicinityAddress_tv.setText((vicinityAddress == null || vicinityAddress.getName() == null) ? "暂无地址信息" : "附近： " + vicinityAddress.getName());
        if (getItem(position).getSignType() != null) {
            viewHolder.signType_tv.setText(Const.getName("TYPE_SIGN_", getItem(position).getSignType()));
            viewHolder.signType_ll.setBackgroundResource(getItem(position).getSignType() == ((Integer) Const.TYPE_SIGN_IN.getValue()) ? R.drawable.sign_in_bg : R.drawable.sign_out_bg);
        }
        viewHolder.signTime_tv.setText(DateUtil.dateToString("HH:mm", getItem(position).getSignTime()));
        return view;
    }

    static class ViewHolder {
        @ViewInject(R.id.signDate_tv)
        private TextView signDate_tv;
        @ViewInject(R.id.signDate_line)
        private View signDate_line;
        @ViewInject(R.id.signType_ll)
        private LinearLayout signType_ll;
        @ViewInject(R.id.signType_tv)
        private TextView signType_tv;
        @ViewInject(R.id.signTime_tv)
        private TextView signTime_tv;
        @ViewInject(R.id.signAddress_tv)
        private TextView signAddress_tv;
        @ViewInject(R.id.vicinityAddress_tv)
        private TextView vicinityAddress_tv;
    }
}
