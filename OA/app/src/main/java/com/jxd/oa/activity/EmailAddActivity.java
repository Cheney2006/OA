package com.jxd.oa.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Email;
import com.jxd.oa.bean.User;
import com.jxd.oa.constants.Const;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.ParamManager;
import com.jxd.oa.view.AttachmentAddView;
import com.jxd.oa.view.SelectEditView;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.WhereBuilder;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;
import com.yftools.util.AndroidUtil;
import com.yftools.util.FileUtil;
import com.yftools.util.StorageUtil;
import com.yftools.util.UUIDGenerator;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.IOException;
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
    @ViewInject(R.id.title_et)
    private EditText title_et;
    @ViewInject(R.id.content_et)
    private EditText content_et;
    @ViewInject(R.id.important_sev)
    private SelectEditView important_sev;
    @ViewInject(R.id.recipient_sev)
    private SelectEditView recipient_sev;
    @ViewInject(R.id.email_aav)
    private AttachmentAddView email_aav;
    private HashMap<String, User> selectedMap;
    private Email email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_add);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_title_email_add));
        initView();
        initData();
    }

    private void initData() {
        if (getIntent().hasExtra("emailId")) {//草稿或者抄送
            try {
                email = DbOperationManager.getInstance().getBeanById(Email.class, getIntent().getStringExtra("emailId"));
                if (!TextUtils.isEmpty(email.getTitle())) {
                    title_et.setText(email.getTitle());
                }
                if (!TextUtils.isEmpty(email.getContent())) {
                    content_et.setText(Html.fromHtml(email.getContent()));
                }
                if (!TextUtils.isEmpty(email.getImportant())) {
                    important_sev.setContent(Const.getName("TYPE_IMPORTANT_", email.getImportant()), email.getImportant());
                }
                if (email.getToIds() != null) {
                    String[] ids = email.getToIds().split(",");
                    if (ids != null) {
                        if (selectedMap == null) {
                            selectedMap = new HashMap<String, User>();
                        }
                        StringBuffer name_sb = new StringBuffer();
                        for (String id : ids) {
                            if (!id.equals(SysConfig.getInstance().getUserId())) {
                                User user = DbOperationManager.getInstance().getBeanById(User.class, id);
                                if (user != null) {
                                    name_sb.append(user.getName()).append(";");
                                    selectedMap.put(id, user);
                                }
                            }
                        }
                        recipient_sev.setContent(name_sb.toString(), email.getToIds());
                    }
                }
                if (email.getAttachmentName() != null) {
                    String[] attachmentNames = email.getAttachmentName().split("\\|");
                    if (attachmentNames != null) {
                        for (String attachmentName : attachmentNames) {
                            email_aav.addAttachment(attachmentName);
                        }
                    }
                }
            } catch (DbException e) {
                LogUtil.e(e);
            }
        }
    }

    private void initView() {
        email_aav.setFileChooseListener(this);
    }

    @OnClick(R.id.recipient_sev)
    public void recipientClick(View view) {
        Intent intent = new Intent(mContext, UserSelectActivity.class);
        if (selectedMap != null) {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveDraft();
                break;
            case R.id.action_save:
                if (validate() && setData()) {
                    submit();
                }
                break;
        }
        return true;
    }

    private void saveDraft() {
        if (email != null && email.getLocalId() != null) {
            finish();
            return;
        }
        //是否存为草稿
        new JxdAlertDialog(mContext, getString(R.string.txt_tips), "是否存为草稿？", getString(R.string.txt_yes), getString(R.string.txt_no), getString(R.string.txt_cancel)) {
            @Override
            protected void positive() {
                if (validate() && setData()) {
                    //设置ID
                    email.setId(UUIDGenerator.getUUID());
                    email.setLocalId(email.getId());
                    email.setAttachmentName(email_aav.getAttachmentName());
                    try {
                        DbOperationManager.getInstance().save(email);
                        //sendBroadcast(new Intent(Constant.ACTION_REFRESH));
                        setResult(RESULT_OK);
                        finish();
                    } catch (DbException e) {
                        LogUtil.e(e);
                    }
                }
            }

            @Override
            protected void neutral() {
                finish();
            }
        }.show();
    }

    private boolean setData() {
        email = new Email();
        email.setTitle(title_et.getText().toString());
        email.setContent(content_et.getText().toString());
        email.setToIds(recipient_sev.getValue() + "");
        email.setFromId(SysConfig.getInstance().getUserId());
        if (important_sev.getValue() != null) {
            email.setImportant(important_sev.getValue() + "");
        }
        return true;
    }

    private void submit() {
        RequestParams params = ParamManager.setDefaultParams();
        if (email.getLocalId() != null) {//草稿上传时，取消本地id
            email.setId(null);
        }
        params.addBodyParameter("data", GsonUtil.getInstance().getGson().toJson(email));
        if (email_aav.getFilePathList() != null) {
            for (String filePath : email_aav.getFilePathList()) {
                File file = new File(filePath);
                if (file.exists()) {
                    params.addBodyParameter("attachments", new File(filePath));
                }
            }
        }
        HttpUtil.getInstance().sendInDialog(mContext, getString(R.string.txt_is_upload_data), ParamManager.parseBaseUrl("emailSave.action"), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                String result = responseInfo.result.toString();
                Email serverEmail = GsonUtil.getInstance().getGson().fromJson(result, Email.class);
                try {
                    //复制文件到项目目录
                    email_aav.copyFile(serverEmail.getAttachmentName());
                    //如果是草稿的提交，则先更新id,再保存对象
                    if (email.getLocalId() != null) {
                        DbOperationManager.getInstance().execSql("UPDATE t_email SET id = '" + serverEmail.getId() + "' WHERE localId = '" + email.getLocalId() + "' ");
                        //DbOperationManager.getInstance().update(serverEmail, WhereBuilder.b().expr("localId", "=", email.getLocalId()), "id");
                    }
                    DbOperationManager.getInstance().save(serverEmail);
                    //sendBroadcast(new Intent(Constant.ACTION_REFRESH));
                    setResult(RESULT_OK);
                    finish();
                } catch (DbException e) {
                    LogUtil.e(e);
                } catch (IOException e) {
                    LogUtil.e(e);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
            }
        });
    }

    private boolean validate() {
        if (TextUtils.isEmpty(title_et.getText())) {
            displayToast("请输入邮件主题");
            return false;
        }
        if (recipient_sev.getValue() == null) {
            displayToast("请选择接收人");
            return false;
        }
        if (important_sev.getValue() == null) {
            displayToast("请选择重要性");
            return false;
        }
        if (TextUtils.isEmpty(content_et.getText())) {
            displayToast("请输入邮件内容");
            return false;
        }
        return true;
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
                    email_aav.addAttachment(path);
                    break;
                case CODE_USER_SELECT:
                    selectedMap = (HashMap<String, User>) data.getSerializableExtra("selectedData");
                    StringBuffer name_sb = new StringBuffer(), value_sb = new StringBuffer();
                    for (User user : selectedMap.values()) {
                        name_sb.append(user.getName()).append(";");
                        value_sb.append(user.getId()).append(",");
                    }
                    recipient_sev.setContent(name_sb.toString(), value_sb.toString());
                    break;
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveDraft();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
