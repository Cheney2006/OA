package com.jxd.oa.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.ExpenseAccount;
import com.jxd.oa.constants.Const;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.ParamManager;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

/**
 * *****************************************
 * Description ：审核请假单
 * Created by cy on 2014/10/20.
 * *****************************************
 */
@ContentView(R.layout.activity_expense_account_for_audit)
public class ExpenseAccountForAuditActivity extends AbstractActivity {

    @ViewInject(R.id.itemName_tv)
    private TextView itemName_tv;
    @ViewInject(R.id.type_tv)
    private TextView type_tv;
    @ViewInject(R.id.applyDate_tv)
    private TextView applyDate_tv;
    @ViewInject(R.id.money_tv)
    private TextView money_tv;
    @ViewInject(R.id.remark_tv)
    private TextView remark_tv;
    @ViewInject(R.id.applyUser_tv)
    private TextView applyUser_tv;
    @ViewInject(R.id.auditRemark_et)
    private EditText auditRemark_et;
    private ExpenseAccount expenseAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_title_expense_account_audit));
        expenseAccount = (ExpenseAccount) getIntent().getSerializableExtra("expenseAccount");
        initData();
    }

    public void initData() {
        itemName_tv.setText(expenseAccount.getItemName());
        type_tv.setText(Const.getName("TYPE_EXPENSE_", expenseAccount.getType()));
        applyDate_tv.setText(DateUtil.dateToString(expenseAccount.getApplyDate()));
        money_tv.setText(expenseAccount.getMoney()+"");
        remark_tv.setText(expenseAccount.getRemark());
        if (expenseAccount.getApplyUser() != null) {
            applyUser_tv.setText(expenseAccount.getApplyUser().getName());
        }
    }

    @OnClick(R.id.refuse_btn)
    public void refuseClick(View view){
        expenseAccount.setAuditStatus((Integer) Const.STATUS_AUDIT_REFUSE.getValue());
        submit();
    }

    @OnClick(R.id.agree_btn)
    public void agreeClick(View view){
        expenseAccount.setAuditStatus((Integer) Const.STATUS_AUDIT_PASS.getValue());
        submit();
    }

    private void submit() {
        expenseAccount.setAuditRemark(auditRemark_et.getText().toString());
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("data", GsonUtil.getInstance().getGson().toJson(expenseAccount));
        HttpUtil.getInstance().sendInDialog(mContext, getString(R.string.txt_is_upload_data), ParamManager.parseBaseUrl("expenseAccountSave.action"), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                String result = responseInfo.result.toString();
                ExpenseAccount serverExpenseAccount = GsonUtil.getInstance().getGson().fromJson(result, ExpenseAccount.class);
                try {
                    DbOperationManager.getInstance().saveOrUpdate(serverExpenseAccount);
                    sendRefresh();
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
}
