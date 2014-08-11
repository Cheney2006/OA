package com.jxd.oa.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.User;
import com.jxd.oa.constants.Const;
import com.jxd.oa.view.AttachmentAddView;
import com.jxd.oa.view.SelectEditView;
import com.yftools.ViewUtil;
import com.yftools.util.AndroidUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.List;

/**
 * *****************************************
 * Description ：发送邮件
 * Created by cy on 2014/8/4.
 * *****************************************
 */
public class EmailAddActivity extends AbstractActivity implements AttachmentAddView.FileChooseListener {

    private static final int CODE_FILE_CHOOSE = 101;
    private static final int CODE_USER_SELECT = 102;
    @ViewInject(R.id.important_sev)
    private SelectEditView important_sev;
    @ViewInject(R.id.recipient_sev)
    private SelectEditView recipient_sev;
    @ViewInject(R.id.attachmentView)
    private AttachmentAddView attachmentAddView;
    private HashMap<String, User> selectedMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_add);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getResources().getString(R.string.txt_title_email_add));
        initView();
    }

    private void initView() {
        attachmentAddView.setFileChooseListener(this);

    }

    @OnClick(R.id.recipient_sev)
    public void recipientClick(View view) {
        Intent intent=new Intent(mContext, UserSelectActivity.class);
        if(selectedMap!=null){
            intent.putExtra("selectedData", selectedMap);
        }
        startActivityForResult(intent, CODE_USER_SELECT);
    }

    @OnClick(R.id.important_sev)
    public void importantClick(View view) {
        final List<String> nameList = Const.getNameList("TYPE_IMPORTANT_");
        Dialog alertDialog = new AlertDialog.Builder(this).setTitle("请选择重要性")
                .setItems(nameList.toArray(new String[nameList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        important_sev.setContent(nameList.get(which), Const.getValueList("TYPE_IMPORTANT_").get(which) + "");
                    }
                }).create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFileChoose() {
        AndroidUtil.showFileChooser(this, CODE_FILE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_FILE_CHOOSE:
                    Uri uri = data.getData();
                    String path = AndroidUtil.getPath(mContext, uri);
                    attachmentAddView.addAttachment(path);
                    break;
                case CODE_USER_SELECT:
                   selectedMap = (HashMap<String, User>) data.getSerializableExtra("selectedData");
                    StringBuffer name_sb = new StringBuffer(), value_sb = new StringBuffer();
                    for (User user : selectedMap.values()) {
                        name_sb.append(user.getName()).append(",");
                        value_sb.append(user.getId()).append(",");
                    }
                    if (name_sb.length() > 0) {
                        name_sb.deleteCharAt(name_sb.length() - 1);
                    }
                    if (value_sb.length() > 0) {
                        value_sb.deleteCharAt(value_sb.length() - 1);
                    }
                    recipient_sev.setContent(name_sb.toString(), value_sb.toString());
                    break;
            }
        }
    }
}
