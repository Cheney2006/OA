package com.jxd.oa.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.ExpenseAccount;
import com.jxd.oa.constants.Const;
import com.yftools.ViewUtil;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;

/**
 * *****************************************
 * Description ：报销单详情
 * Created by cy on 2014/10/26.
 * *****************************************
 */
@ContentView(R.layout.activity_leave_expense_account)
public class ExpenseAccountDetailActivity extends AbstractActivity {

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
    @ViewInject(R.id.auditUser_tv)
    private TextView auditUser_tv;
    @ViewInject(R.id.auditStatus_tv)
    private TextView auditStatus_tv;
    @ViewInject(R.id.auditRemark_tv)
    private TextView auditRemark_tv;
    private ExpenseAccount expenseAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_title_expense_account_detail));
        expenseAccount = (ExpenseAccount) getIntent().getSerializableExtra("expenseAccount");
        initData();
    }

    public void initData() {
        itemName_tv.setText(expenseAccount.getItemName());
        type_tv.setText(Const.getName("TYPE_EXPENSE_", expenseAccount.getType()));
        applyDate_tv.setText(DateUtil.dateToString(expenseAccount.getApplyDate()));
        money_tv.setText(expenseAccount.getMoney() + "");
        remark_tv.setText(expenseAccount.getRemark());
        if (expenseAccount.getApplyUser() != null) {
            applyUser_tv.setText(expenseAccount.getApplyUser().getName());
        }
        if (expenseAccount.getAuditUser() != null) {
            auditUser_tv.setText(expenseAccount.getAuditUser().getName());
        }
        auditRemark_tv.setText(expenseAccount.getAuditRemark());
        if (expenseAccount.getAuditStatus() == Const.STATUS_REFUSE.getValue()) {
            auditStatus_tv.setTextColor(mContext.getResources().getColor(R.color.color_red));
        } else if (expenseAccount.getAuditStatus() == Const.STATUS_PASS.getValue()) {
            auditStatus_tv.setTextColor(mContext.getResources().getColor(R.color.color_being));
        } else {
            auditStatus_tv.setTextColor(mContext.getResources().getColor(R.color.color_black_font));
        }
        auditStatus_tv.setText(Const.getName("STATUS_", expenseAccount.getAuditStatus()));
    }

}
