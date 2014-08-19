package com.jxd.oa.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jxd.oa.R;
import com.jxd.oa.fragment.base.AbstractFragment;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ViewInject;

/**
 * *****************************************
 * Description ：企业云——公司文档
 * Created by cy on 2014/8/18.
 * *****************************************
 */
public class CloudFragment extends AbstractFragment {

    @ViewInject(R.id.mListView)
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.activity_list_view, container, false);
        ViewUtil.inject(this, convertView);
        return convertView;
    }
}
