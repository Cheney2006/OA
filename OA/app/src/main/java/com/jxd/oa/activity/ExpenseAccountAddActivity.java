package com.jxd.oa.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.ExpenseAccount;
import com.jxd.oa.bean.User;
import com.jxd.oa.constants.Const;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.ParamManager;
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
import com.yftools.ui.DatePickUtil;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.List;

/**
 * *****************************************
 * Description ：增加报销单
 * Created by cy on 2014/10/15.
 * *****************************************
 */
@ContentView(R.layout.activity_expense_account_add)
public class ExpenseAccountAddActivity extends AbstractActivity {

    private static final int CODE_USER_SELECT = 101;

    @ViewInject(R.id.itemName_et)
    private EditText itemName_et;
    @ViewInject(R.id.type_sev)
    private SelectEditView type_sev;
    @ViewInject(R.id.applyDate_sev)
    private SelectEditView applyDate_sev;
    @ViewInject(R.id.money_et)
    private EditText money_et;
    @ViewInject(R.id.remark_et)
    private EditText remark_et;
    @ViewInject(R.id.auditUser_sev)
    private SelectEditView auditUser_sev;
    private ExpenseAccount expenseAccount;
    private HashMap<String, User> selectedMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_title_expense_account_add));
    }

    @OnClick(R.id.type_sev)
    public void typeClick(View view) {
        final List<String> nameList = Const.getNameList("TYPE_EXPENSE_");
        Dialog alertDialog = new AlertDialog.Builder(this).setTitle("请选择类型")
                .setItems(nameList.toArray(new String[nameList.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        type_sev.setContent(nameList.get(which), Const.getValueList("TYPE_LEAVE_").get(which) + "");
                    }
                }).create();
        alertDialog.show();
    }

    @OnClick(R.id.auditUser_sev)
    public void auditUserClick(View view) {
        Intent intent = new Intent(mContext, UserSelectActivity.class);
        if (selectedMap != null) {
            intent.putExtra("selectedData", selectedMap);
        }
        intent.putExtra("isMulti", false);
        startActivityForResult(intent, CODE_USER_SELECT);
    }

    @OnClick(R.id.startDate_sev)
    public void startDateClick(View view) {
        new DatePickUtil(mContext, "请选择报销日期", new DatePickUtil.DateSetFinished() {
            @Override
            public void onDateSetFinished(String pickYear, String pickMonth, String pickDay) {
                applyDate_sev.setContent(pickYear + "-" + pickMonth + "-" + pickDay);
            }
        }).showDateDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (validate() && setData()) {
                    submit();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("data", GsonUtil.getInstance().getGson().toJson(expenseAccount));
        HttpUtil.getInstance().sendInDialog(mContext, getString(R.string.txt_is_upload_data), ParamManager.parseBaseUrl("expenseAccountSave.action"), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                String result = responseInfo.result.toString();
                ExpenseAccount serverExpenseAccount = GsonUtil.getInstance().getGson().fromJson(result, ExpenseAccount.class);
                try {
                    DbOperationManager.getInstance().saveOrUpdate(serverExpenseAccount);
                    setResult(RESULT_OK);
                    finish();
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

    private boolean setData() {
        expenseAccount = new ExpenseAccount();
        expenseAccount.setItemName(itemName_et.getText().toString());
        expenseAccount.setType(Integer.parseInt(type_sev.getValue().toString()));
        expenseAccount.setApplyDate(DateUtil.stringToDate(applyDate_sev.getValue().toString()));
        expenseAccount.setMoney(Double.parseDouble(money_et.getText().toString()));
        expenseAccount.setRemark(remark_et.getText().toString());
        User user = new User();
        user.setId(SysConfig.getInstance().getUserId());
        expenseAccount.setApplyUser(user);
        User auditUser = new User();
        auditUser.setId(auditUser_sev.getValue().toString());
        expenseAccount.setAuditUser(auditUser);
        expenseAccount.setAuditStatus((Integer) Const.STATUS_AUDIT_BEING.getValue());
        expenseAccount.setModifiedDate(getNowDate());
        return true;
    }

    private boolean validate() {
        if (TextUtils.isEmpty(itemName_et.getText())) {
            displayToast("请输入请假事由");
            return false;
        }
        if (type_sev.getValue() == null) {
            displayToast("请选择类型");
            return false;
        }
        if (applyDate_sev.getValue() == null) {
            displayToast("请选择开始时间");
            return false;
        }
        if (TextUtils.isEmpty(money_et.getText())) {
            displayToast("请输入报销金额");
            return false;
        }
        if (auditUser_sev.getValue() == null) {
            displayToast("请选择审批人");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_USER_SELECT:
                    selectedMap = (HashMap<String, User>) data.getSerializableExtra("selectedData");
                    if (selectedMap.size() > 1) {
                        displayToast("审核人只能一个");
                        return;
                    }
                    StringBuffer name_sb = new StringBuffer(), value_sb = new StringBuffer();
                    for (User user : selectedMap.values()) {
                        name_sb.append(user.getName());
                        value_sb.append(user.getId());
                    }
                    auditUser_sev.setContent(name_sb.toString(), value_sb.toString());
                    break;
            }
        }
    }

}
