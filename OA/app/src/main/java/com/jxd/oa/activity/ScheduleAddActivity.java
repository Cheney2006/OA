package com.jxd.oa.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Schedule;
import com.jxd.oa.constants.Const;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.ParamManager;
import com.jxd.oa.view.AttachmentAddView;
import com.jxd.oa.view.SelectEditView;
import com.jxd.oa.view.TypeView;
import com.jxd.oa.view.timepicker.DateTimePickUtil;
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
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * *****************************************
 * Description ：增加今日日程
 * Created by cy on 2014/8/18.
 * *****************************************
 */
public class ScheduleAddActivity extends AbstractActivity {

    @ViewInject(R.id.title_et)
    private TextView title_et;
    @ViewInject(R.id.important_sev)
    private SelectEditView important_sev;
    @ViewInject(R.id.category_tv)
    private TypeView category_tv;
    @ViewInject(R.id.startDate_sev)
    private SelectEditView startDate_sev;
    @ViewInject(R.id.endDate_sev)
    private SelectEditView endDate_sev;
    @ViewInject(R.id.content_et)
    private EditText content_et;
    @ViewInject(R.id.schedule_aav)
    private AttachmentAddView schedule_aav;
    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_add);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_title_schedule_add));
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

    @OnClick(R.id.startDate_sev)
    public void startDateClick(View view) {
        new DateTimePickUtil(mContext, "请选择开始时间", true, new DateTimePickUtil.DateTimeSetFinished() {
            @Override
            public void onDateTimeSetFinished(String dateTime) {
                startDate_sev.setContent(dateTime);
            }
        }).showDateDialog();
    }

    @OnClick(R.id.endDate_sev)
    public void endDateClick(View view) {
        new DateTimePickUtil(mContext, "请选择结束时间", true, new DateTimePickUtil.DateTimeSetFinished() {
            @Override
            public void onDateTimeSetFinished(String dateTime) {
                if (startDate_sev.getValue() == null) {
                    displayToast("请先选择开始时间");
                } else {
                    if (dateTime.compareTo(startDate_sev.getValue().toString()) < 0) {
                        displayToast("结束时间必须大于开始时间");
                    } else {
                        endDate_sev.setContent(dateTime);
                    }
                }
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
        params.addBodyParameter("data", GsonUtil.getInstance().getGson().toJson(schedule));
        if (schedule_aav.getFilePathList() != null) {
            for (String filePath : schedule_aav.getFilePathList()) {
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
                Schedule serverSchedule = GsonUtil.getInstance().getGson().fromJson(result, Schedule.class);
                try {
                    //复制文件到项目目录
                    schedule_aav.copyFile(serverSchedule.getAttachmentName());
                    DbOperationManager.getInstance().save(serverSchedule);
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

    private boolean setData() {
        schedule = new Schedule();
        schedule.setTitle(title_et.getText().toString());
        schedule.setImportant(important_sev.getValue().toString());
        //TODO 设置类型
//        ContactsCategory contactsCategory = new ContactsCategory();
//        contactsCategory.setGroupName(category_tv.getContent());
//        if (category_tv.getValue() != null) {
//            contactsCategory.setId(category_tv.getValue() + "");
//        }
//        schedule.setType(category_tv.getValue().toString());
        schedule.setStartData(DateUtil.stringToDate("yyyy-MM-dd HH:mm", startDate_sev.getValue().toString()));
        schedule.setEndData(DateUtil.stringToDate("yyyy-MM-dd HH:mm", endDate_sev.getValue().toString()));
        schedule.setContent(content_et.getText().toString());
        return true;
    }

    private boolean validate() {
        if (TextUtils.isEmpty(title_et.getText())) {
            displayToast("请输入主题");
            return false;
        }
        if (important_sev.getValue() == null) {
            displayToast("请选择重要性");
            return false;
        }
        if (startDate_sev.getValue() == null) {
            displayToast("请选择开始时间");
            return false;
        }
        if (endDate_sev.getValue() == null) {
            displayToast("请选择结束时间");
            return false;
        }
        if (endDate_sev.getValue().toString().compareTo(startDate_sev.getValue().toString()) < 0) {
            displayToast("结束时间必须大于开始时间");
            return false;
        }
        return true;
    }
}
