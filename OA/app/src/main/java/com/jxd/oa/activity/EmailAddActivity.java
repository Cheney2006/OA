package com.jxd.oa.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Email;
import com.jxd.oa.bean.User;
import com.jxd.oa.constants.Const;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.ParamManager;
import com.jxd.oa.view.AttachmentAddView;
import com.jxd.oa.view.SelectEditView;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;
import com.yftools.util.AndroidUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import org.apache.http.protocol.HTTP;

import java.io.File;
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
        getSupportActionBar().setTitle(getResources().getString(R.string.txt_title_email_add));
        initView();
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
                break;
            case R.id.action_save:
                if (validate() && setData()) {
                    submit();
                }
                break;
        }
        return true;
    }

    private boolean setData() {
        email = new Email();
        email.setTitle(title_et.getText().toString());
        email.setContent(content_et.getText().toString());
        email.setToIds(recipient_sev.getValue() + "");
        if (important_sev.getValue() != null) {
            email.setImportant(important_sev.getValue() + "");
        }
        return true;
    }

    private void submit() {
        RequestParams params = ParamManager.setDefaultParams();
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
                String result = responseInfo.result.getString("data");
                Email email = GsonUtil.getInstance().getGson().fromJson(result, Email.class);
                try {
                    DbOperationManager.getInstance().save(email);
                } catch (DbException e) {
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
            displayToast("请选择邮件主题");
            return false;
        }
        if (recipient_sev.getValue() == null) {
            displayToast("请选择接收人");
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
