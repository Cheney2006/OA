package com.jxd.oa.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.Cloud;
import com.jxd.oa.bean.Contact;
import com.jxd.oa.bean.Notice;
import com.jxd.oa.constants.Constant;
import com.yftools.HttpUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.HttpException;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.util.AndroidUtil;
import com.yftools.util.DateUtil;
import com.yftools.util.DigitUtil;
import com.yftools.util.FileUtil;
import com.yftools.util.StorageUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import java.io.File;
import java.util.List;

/**
 * *****************************************
 * Description ：企业云
 * Created by cy on 2014/8/6.
 * *****************************************
 */
public class CloudAdapter extends AbstractAdapter<Cloud> {

    public CloudAdapter(Context context, List<Cloud> dataList) {
        super(context, dataList);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        Cloud data = getItem(position);
        if (view == null) {
            viewHolder = new ViewHolder(data);
            view = getInflater().inflate(R.layout.item_cloud, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            viewHolder.update(data);
        }
        viewHolder.name_tv.setText(getItem(position).getFileName());
        viewHolder.size_tv.setText(FileUtil.formatFileSize(getItem(position).getFileSize()));
        return view;
    }

    public class ViewHolder {
        private Cloud data;

        public ViewHolder(Cloud data) {
            this.data = data;
        }

        public void update(Cloud data) {
            this.data = data;
        }

        @ViewInject(R.id.name_tv)
        private TextView name_tv;
        @ViewInject(R.id.size_tv)
        private TextView size_tv;
        @ViewInject(R.id.download_tv)
        public TextView download_tv;

    }
}
