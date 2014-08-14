package com.jxd.oa.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jxd.common.vo.Item;
import com.jxd.oa.R;
import com.jxd.oa.constants.Const;

import java.util.List;

/**
 * *****************************************
 * Description ：类型选择
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
public class TypeView extends LinearLayout {

    private SelectEditView type_sev;
    private List<Item> dataList;
    private LayoutInflater inflate;

    public TypeView(Context context) {
        super(context);
    }

    public TypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView() {
        this.setOrientation(VERTICAL);
        inflate = LayoutInflater.from(getContext());
        inflate.inflate(R.layout.view_type, this);
        type_sev = (SelectEditView) findViewById(R.id.type_sev);
        type_sev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] nameArray = new String[1];
                int i = 0;
                if (dataList != null) {
                    nameArray = new String[dataList.size() + 1];
                    for (Item item : dataList) {
                        nameArray[i] = item.getText();
                        i++;
                    }
                }
                nameArray[i] = "自定义";
                final int finalI = i;
                Dialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("请选择重要性")
                        .setItems(nameArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which < finalI) {
                                    type_sev.setContent(dataList.get(which).getText(), dataList.get(which).getValue() + "");
                                } else {
                                    //自定义增加类型
                                    View view = inflate.inflate(R.layout.dialog_edit, null);
                                    final EditText typeName_et = (EditText) view.findViewById(R.id.typeName_et);
                                    new AlertDialog.Builder(getContext()).setTitle("请输入")
                                            .setView(typeName_et).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (TextUtils.isEmpty(typeName_et.getText())) {
                                                Toast.makeText(getContext(), "请输入名称", Toast.LENGTH_SHORT).show();
                                            } else {
                                                type_sev.setContent(typeName_et.getText().toString());
                                            }
                                        }
                                    }).setNegativeButton("取消", null).create().show();
                                }
                            }
                        }).create();
                alertDialog.show();
            }
        });
    }

    public void initData(List<Item> dataList) {
        this.dataList = dataList;
    }

    public void setValue(String content, String value) {
        type_sev.setContent(content, value);
    }

    public String getContent() {
        return type_sev.getContent();
    }

    public Object getValue() {
        return type_sev.getValue();
    }
}
