package com.jxd.oa.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

/**
 * *****************************************
 * Description ：同步数据adapter
 * Created by cy on 2014/7/30.
 * *****************************************
 */
public class SyncDataAdapter extends AbstractAdapter<String> {

    private int syncingIndex = -1;

    public SyncDataAdapter(Context context, String[] arrayOfT) {
        super(context, arrayOfT);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getInflater().inflate(R.layout.item_sync_data, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name_tv.setText(getItem(position));
        if (syncingIndex == position) {
            viewHolder.syncing_pb.setVisibility(View.VISIBLE);
            viewHolder.syncFinished_iv.setVisibility(View.GONE);
            viewHolder.syncWaiting_tv.setVisibility(View.GONE);
        } else if (syncingIndex > position) {
            viewHolder.syncing_pb.setVisibility(View.GONE);
            viewHolder.syncFinished_iv.setVisibility(View.VISIBLE);
            viewHolder.syncWaiting_tv.setVisibility(View.GONE);
        }else{
            viewHolder.syncing_pb.setVisibility(View.GONE);
            viewHolder.syncFinished_iv.setVisibility(View.GONE);
            viewHolder.syncWaiting_tv.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public void setSyncingIndex(int syncingIndex) {
        this.syncingIndex = syncingIndex;
    }

    static class ViewHolder {
        @ViewInject(R.id.name_tv)
        private TextView name_tv;
        @ViewInject(R.id.syncWaiting_tv)
        private TextView syncWaiting_tv;
        @ViewInject(R.id.syncFinished_iv)
        private ImageView syncFinished_iv;
        @ViewInject(R.id.syncing_pb)
        private ProgressBar syncing_pb;
    }

}
