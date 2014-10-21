package com.jxd.oa.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.LeaveApplication;
import com.jxd.oa.constants.Const;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;

/**
 * *****************************************
 * Description ：请假单详情
 * Created by cy on 2014/10/16.
 * *****************************************
 */
@ContentView(R.layout.activity_leave_application_detail)
public class LeaveApplicationDetailActivity extends AbstractActivity {

    @ViewInject(R.id.leaveReason_tv)
    private TextView leaveReason_tv;
    @ViewInject(R.id.type_tv)
    private TextView type_tv;
    @ViewInject(R.id.starDate_tv)
    private TextView starDate_tv;
    @ViewInject(R.id.endDate_tv)
    private TextView endDate_tv;
    @ViewInject(R.id.applyUser_tv)
    private TextView applyUser_tv;
    @ViewInject(R.id.auditUser_tv)
    private TextView auditUser_tv;
    @ViewInject(R.id.auditStatus_tv)
    private TextView auditStatus_tv;
    @ViewInject(R.id.auditRemark_tv)
    private TextView auditRemark_tv;
    private LeaveApplication leaveApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_title_leave_application_detail));
        leaveApplication = (LeaveApplication) getIntent().getSerializableExtra("leaveApplication");
        initData();
    }

    public void initData() {
        leaveReason_tv.setText(leaveApplication.getLeaveReason());
        type_tv.setText(Const.getName("TYPE_LEAVE_", leaveApplication.getType()));
        starDate_tv.setText(leaveApplication.getStartDate());
        endDate_tv.setText(leaveApplication.getEndDate());
        if (leaveApplication.getApplyUser() != null) {
            applyUser_tv.setText(leaveApplication.getApplyUser().getName());
        }
        if (leaveApplication.getAuditUser() != null) {
            auditUser_tv.setText(leaveApplication.getAuditUser().getName());
        }
        auditRemark_tv.setText(leaveApplication.getAuditRemark());
        if (leaveApplication.getAuditStatus() == Const.STATUS_REFUSE.getValue()) {
            auditStatus_tv.setTextColor(mContext.getResources().getColor(R.color.color_red));
        } else if (leaveApplication.getAuditStatus() == Const.STATUS_PASS.getValue()) {
            auditStatus_tv.setTextColor(mContext.getResources().getColor(R.color.color_being));
        } else {
            auditStatus_tv.setTextColor(mContext.getResources().getColor(R.color.color_black_font));
        }
        auditStatus_tv.setText(Const.getName("STATUS_", leaveApplication.getAuditStatus()));
    }

}
