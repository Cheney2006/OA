package com.jxd.common.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jxd.common.R;
import com.jxd.common.vo.Item;

/**
 ****************************************************** 
 * @Description : 下拉选择适配器
 * @Author : cy cy20061121@163.com
 * @Creation Date : 2013-5-22 下午8:41:41
 ****************************************************** 
 */
public class PopListAdapter extends BaseAdapter {
	public List<Item> datas;
	Context context;
	LayoutInflater inflater;

	public PopListAdapter(Context context, List<Item> datas) {
		super();
		this.datas = datas;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas != null ? datas.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return datas != null ? datas.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.pop_list_item, null);
		}
		TextView type_name = (TextView) convertView.findViewById(R.id.item_name);
		if (datas != null) {
			type_name.setText(datas.get(position).toString());
		}
		return convertView;
	}
}
