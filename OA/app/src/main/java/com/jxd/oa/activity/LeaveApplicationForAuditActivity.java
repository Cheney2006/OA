package com.jxd.oa.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.LeaveApplication;
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
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

/**
 * *****************************************
 * Description ：审核请假单
 * Created by cy on 2014/10/20.
 * *****************************************
 */
@ContentView(R.layout.activity_leave_application_detail)
public class LeaveApplicationForAuditActivity extends AbstractActivity {

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
    @ViewInject(R.id.auditRemark_et)
    private EditText auditRemark_et;
    private LeaveApplication leaveApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_title_leave_application_audit));
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
    }

    @OnClick(R.id.refuse_btn)
    public void refuseClick(View view){
        leaveApplication.setAuditStatus((Integer) Const.STATUS_REFUSE.getValue());
        submit();
    }

    @OnClick(R.id.agree_btn)
    public void agreeClick(View view){
        leaveApplication.setAuditStatus((Integer) Const.STATUS_PASS.getValue());
        submit();
    }

    private void submit() {
        leaveApplication.setAuditRemark(auditRemark_et.getText().toString());
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("data", GsonUtil.getInstance().getGson().toJson(leaveApplication));
        HttpUtil.getInstance().sendInDialog(mContext, getString(R.string.txt_is_upload_data), ParamManager.parseBaseUrl("leaveApplicationSave.action"), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                String result = responseInfo.result.toString();
                LeaveApplication serverLeaveApplication = GsonUtil.getInstance().getGson().fromJson(result, LeaveApplication.class);
                try {
                    DbOperationManager.getInstance().saveOrUpdate(serverLeaveApplication);
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
}
