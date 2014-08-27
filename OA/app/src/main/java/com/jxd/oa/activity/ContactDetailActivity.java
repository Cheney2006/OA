package com.jxd.oa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Contact;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.ParamManager;
import com.jxd.oa.view.PhoneView;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

/**
 * *****************************************
 * Description ：通讯录详情
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
public class ContactDetailActivity extends AbstractActivity {

    @ViewInject(R.id.name_tv)
    private TextView name_tv;
    @ViewInject(R.id.ministration_tv)
    private TextView ministration_tv;
    @ViewInject(R.id.category_tv)
    private TextView category_tv;
    @ViewInject(R.id.companyName_tv)
    private TextView companyName_tv;
    @ViewInject(R.id.companyAddr_tv)
    private TextView companyAddr_tv;
    @ViewInject(R.id.sex_tv)
    private TextView sex_tv;
    @ViewInject(R.id.mobile_pv)
    private PhoneView mobile_pv;
    @ViewInject(R.id.homeTel_pv)
    private PhoneView homeTle_pv;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("通讯录详情");
        contact = (Contact) getIntent().getSerializableExtra("contact");
        initView();
        initData();
    }

    private void initView() {
        mobile_pv.setLabel(getString(R.string.txt_label_mobile));
        homeTle_pv.setLabel(getString(R.string.txt_label_phone));
    }

    private void initData() {
        name_tv.setText(contact.getName());
        ministration_tv.setText(contact.getMinistration());
        if (contact.getCategory() != null) {
            category_tv.setText(contact.getCategory().getGroupName());
        }
        companyName_tv.setText(contact.getCompanyName());
        companyAddr_tv.setText(contact.getCompanyAddr());
        sex_tv.setText(contact.getSex());
        if (TextUtils.isEmpty(contact.getMobile())) {
            mobile_pv.setVisibility(View.GONE);
        } else {
            mobile_pv.setVisibility(View.VISIBLE);
            mobile_pv.initPhone(contact.getMobile());
        }
        if (TextUtils.isEmpty(contact.getHomeTel())) {
            homeTle_pv.setVisibility(View.GONE);
        } else {
            homeTle_pv.setVisibility(View.VISIBLE);
            homeTle_pv.initPhone(contact.getHomeTel());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(mContext, ContactAddActivity.class);
                intent.putExtra("contact", contact);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.contactDelete_btn)
    public void contactDeleteClick(View view) {
        new JxdAlertDialog(mContext, getString(R.string.txt_tips), "确定删除联系人", getString(R.string.txt_confirm), getString(R.string.txt_cancel)) {
            @Override
            protected void positive() {
                RequestParams params = ParamManager.setDefaultParams();
                params.addBodyParameter("id", contact.getId());
                HttpUtil.getInstance().sendInDialog(mContext, "正在删除联系人...", ParamManager.parseBaseUrl("contactDelete.action"), params, new RequestCallBack<Json>() {
                    @Override
                    public void onSuccess(ResponseInfo<Json> responseInfo) {
                        try {
                            DbOperationManager.getInstance().deleteBean(contact);
                            sendBroadcast(new Intent(Constant.ACTION_REFRESH));
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
        }.show();
    }
}
