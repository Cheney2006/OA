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
import com.jxd.oa.bean.Contacts;
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
import com.yftools.util.ListUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

/**
 * *****************************************
 * Description ：通讯录详情
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
public class ContactsDetailActivity extends AbstractActivity {

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
    private Contacts contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("通讯录详情");
        contacts = (Contacts) getIntent().getSerializableExtra("contacts");
        initData();
    }

    private void initData() {
        name_tv.setText(contacts.getName());
        ministration_tv.setText(contacts.getMinistration());
        if (contacts.getCategory() != null) {
            category_tv.setText(contacts.getCategory().getGroupName());
        }
        companyName_tv.setText(contacts.getCompanyName());
        companyAddr_tv.setText(contacts.getCompanyAddr());
        sex_tv.setText(contacts.getSex());
        if (TextUtils.isEmpty(contacts.getMobile())) {
            mobile_pv.setVisibility(View.GONE);
        } else {
            mobile_pv.setVisibility(View.VISIBLE);
            mobile_pv.initPhone(contacts.getMobile());
        }
        if (TextUtils.isEmpty(contacts.getHomeTel())) {
            homeTle_pv.setVisibility(View.GONE);
        } else {
            homeTle_pv.setVisibility(View.VISIBLE);
            homeTle_pv.initPhone(contacts.getHomeTel());
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
                Intent intent = new Intent(mContext, ContactsAddActivity.class);
                intent.putExtra("contacts", contacts);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.contactsDelete_btn)
    public void contactsDeleteClick(View view) {
        new JxdAlertDialog(mContext, getString(R.string.txt_tips), "确定删除联系人", getString(R.string.txt_confirm), getString(R.string.txt_cancel)) {
            @Override
            protected void positive() {
                RequestParams params = ParamManager.setDefaultParams();
                params.addBodyParameter("id", contacts.getId());
                HttpUtil.getInstance().sendInDialog(mContext, "正在删除联系人...", ParamManager.parseBaseUrl(""), params, new RequestCallBack<Json>() {
                    @Override
                    public void onSuccess(ResponseInfo<Json> responseInfo) {
                        try {
                            DbOperationManager.getInstance().deleteBean(contacts);
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
