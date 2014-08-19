package com.jxd.oa.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jxd.common.vo.Item;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Contacts;
import com.jxd.oa.bean.ContactsCategory;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.ParamManager;
import com.jxd.oa.view.SelectEditView;
import com.jxd.oa.view.TypeView;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;
import com.yftools.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：个人通讯录新增、修改、删除
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
public class ContactsAddActivity extends AbstractActivity {

    @ViewInject(R.id.name_et)
    private EditText name_et;
    @ViewInject(R.id.category_tv)
    private TypeView category_tv;
    @ViewInject(R.id.sex_sev)
    private SelectEditView sex_sev;
    @ViewInject(R.id.mobile_et)
    private EditText mobile_et;
    @ViewInject(R.id.homeTle_et)
    private EditText homeTle_et;
    @ViewInject(R.id.companyName_et)
    private EditText companyName_et;
    @ViewInject(R.id.ministration_et)
    private EditText ministration_et;
    @ViewInject(R.id.companyAddr_et)
    private EditText companyAddr_et;
    private Contacts contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_add);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle("增加联系人");
        contacts = (Contacts) getIntent().getSerializableExtra("contacts");
        initData();
    }

    private void initData() {
        //初始化类型选择
        try {
            List<ContactsCategory> contactsCategoryList = DbOperationManager.getInstance().getBeans(Selector.from(ContactsCategory.class).where("userId", "=", SysConfig.getInstance().getUserId()));
            List<Item> itemList = new ArrayList<Item>();
            for (ContactsCategory contactsCategory : contactsCategoryList) {
                itemList.add(new Item(contactsCategory.getGroupName(), contactsCategory.getId()));
            }
            category_tv.initData(itemList);
        } catch (DbException e) {
            LogUtil.e(e);
        }
        if (contacts != null) {
            name_et.setText(contacts.getName());
            if (contacts.getCategory() != null) {
                category_tv.setValue(contacts.getCategory().getGroupName(), contacts.getCategory().getId());
            }
            sex_sev.setContent(contacts.getSex());
            mobile_et.setText(contacts.getMobile());
            homeTle_et.setText(contacts.getHomeTel());
            companyName_et.setText(contacts.getCompanyName());
            companyAddr_et.setText(contacts.getCompanyAddr());
            ministration_et.setText(contacts.getMinistration());
        }

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
                    contactsSubmit();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void contactsSubmit() {
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("data", GsonUtil.getInstance().getGson().toJson(contacts));
        HttpUtil.getInstance().sendInDialog(mContext, getString(R.string.txt_is_upload_data), ParamManager.parseBaseUrl(""), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                try {
                    DbOperationManager.getInstance().save(GsonUtil.getInstance().getGson().fromJson(responseInfo.result.toString(), Contacts.class));
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
        if (contacts == null) {
            contacts = new Contacts();
        }
        contacts.setName(name_et.getText().toString());
        ContactsCategory contactsCategory = new ContactsCategory();
        contactsCategory.setGroupName(category_tv.getContent());
        if (category_tv.getValue() != null) {
            contactsCategory.setId(category_tv.getValue() + "");
        }
        contacts.setCategory(contactsCategory);
        contacts.setCompanyName(companyName_et.getText().toString());
        contacts.setCompanyAddr(companyAddr_et.getText().toString());
        contacts.setMobile(mobile_et.getText().toString());
        contacts.setHomeTel(homeTle_et.getText().toString());
        contacts.setMinistration(ministration_et.getText().toString());
        contacts.setSex(sex_sev.getValue() + "");
        return true;
    }

    private boolean validate() {
        if (TextUtils.isEmpty(name_et.getText())) {
            displayToast("请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(category_tv.getContent())) {
            displayToast("请选择分组");
            return false;
        }
        if (sex_sev.getValue() != null) {
            displayToast("请选择性别");
            return false;
        }
        if (TextUtils.isEmpty(mobile_et.getText()) && TextUtils.isEmpty(homeTle_et.getText())) {
            displayToast("请至少输入一个联系方式");
            return false;
        }
        return true;
    }
}
