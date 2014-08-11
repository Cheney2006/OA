package com.jxd.oa.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yftools.json.Json;

/**
 * *****************************************
 * Description ：通用的Json数据适配器
 * Created by cy on 13-11-11.
 * *****************************************
 */
public abstract class AbstractJsonAdapter extends BaseAdapter{

    private Context context;
    private Json datas;
    private LayoutInflater inflater;
    protected Json data;

    protected AbstractJsonAdapter(Context context, Json datas) {
        this.context=context;
        this.datas=datas;
        inflater=LayoutInflater.from(context);
    }

    /**
     * @Description : 总数
     */
    @Override
    public int getCount() {
        return datas != null ? datas.getLength() : 0;
    }

    /**
     * @Description : 条目
     */
    @Override
    public Json getItem(int position) {
        return datas != null ? (Json) datas.getItem(position) : null;
    }

    /**
     * @Description : 方法描述
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 在子类中重载
     */
    @Override
    public abstract View getView(int position, View view, ViewGroup viewGroup);

    public void setDatas(Json datas) {
        this.datas = datas;
    }

    public void addData(Object obj){
        this.datas.addItem(obj);
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public Json getDatas() {
        return datas;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }
}
