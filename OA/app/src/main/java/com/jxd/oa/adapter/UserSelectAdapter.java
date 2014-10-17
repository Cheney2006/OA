package com.jxd.oa.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.adapter.base.AbstractAdapter;
import com.jxd.oa.bean.User;
import com.jxd.oa.constants.Constant;
import com.yftools.ViewUtil;
import com.yftools.bitmap.BitmapCommonUtil;
import com.yftools.util.AndroidUtil;
import com.yftools.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;

/**
 * *****************************************
 * Description ：选择用户
 * Created by cy on 2014/8/5.
 * *****************************************
 */
public class UserSelectAdapter extends AbstractAdapter<User> {

    private boolean isMulti=true;
    private HashMap<String, User> selectedMap;

    public UserSelectAdapter(Context context, List<User> dataList) {
        super(context, dataList);
        selectedMap = new HashMap<String, User>();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = getInflater().inflate(R.layout.item_user_select, null);
            ViewUtil.inject(viewHolder, view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name_tv.setText(getItem(position).getName());
        if (getItem(position).getDepartment() != null) {
            viewHolder.department_tv.setText(getItem(position).getDepartment().getDeptName());
        }
        if (getItem(position).getRole() != null) {
            viewHolder.role_tv.setText(getItem(position).getRole().getName());
        }
        if (getItem(position).getPhoto() != null) {
            viewHolder.photo_iv.setImageBitmap(BitmapCommonUtil.toRoundCorner(BitmapFactory.decodeByteArray(getItem(position).getPhoto(), 0, getItem(position).getPhoto().length), AndroidUtil.dip2px(getContext(), Constant.ROUND_CORNER)));
        }
        if(selectedMap.containsKey(getItem(position).getId())){
            viewHolder.selected_cb.setChecked(true);
        }else{
            viewHolder.selected_cb.setChecked(false);
        }
        return view;
    }

    public void select(int position){
        User user=getItem(position);
        if(selectedMap.containsKey(user.getId())){
            selectedMap.remove(user.getId());
        }else {
            if(!isMulti){
                selectedMap=new HashMap<String, User>();
            }
            selectedMap.put(user.getId(),user);
        }
        notifyDataSetChanged();
    }

    public void setSelectedMap(HashMap<String, User> selectedMap) {
        this.selectedMap = selectedMap;
    }

    public boolean isMulti() {
        return isMulti;
    }

    public void setMulti(boolean isMulti) {
        this.isMulti = isMulti;
    }

    public HashMap<String, User> getSelectedMap() {
        return selectedMap;
    }

    static class ViewHolder {
        @ViewInject(R.id.photo_iv)
        private ImageView photo_iv;
        @ViewInject(R.id.name_tv)
        private TextView name_tv;
        @ViewInject(R.id.department_tv)
        private TextView department_tv;
        @ViewInject(R.id.role_tv)
        private TextView role_tv;
        @ViewInject(R.id.selected_cb)
        private CheckBox selected_cb;
    }
}
