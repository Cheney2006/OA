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

import com.jxd.common.vo.Item;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Schedule;
import com.jxd.oa.bean.ScheduleCategory;
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
import com.yftools.util.AndroidUtil;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * *****************************************
 * Description ：增加今日日程
 * Created by cy on 2014/8/18.
 * *****************************************
 */
public class ScheduleAddActivity extends AbstractActivity implements AttachmentAddView.FileChooseListener {

    private static final int CODE_FILE_CHOOSE = 101;

    @ViewInject(R.id.title_et)
    private EditText title_et;
    @ViewInject(R.id.address_et)
    private EditText address_et;
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
        schedule_aav.setFileChooseListener(this);
        initData();
    }

    private void initData() {
        //初始化类型选择
        try {
            List<ScheduleCategory> scheduleCategoryList = DbOperationManager.getInstance().getBeans(ScheduleCategory.class);
            List<Item> itemList = new ArrayList<Item>();
            for (ScheduleCategory scheduleCategory : scheduleCategoryList) {
                itemList.add(new Item(scheduleCategory.getName(), scheduleCategory.getId()));
            }
            category_tv.initData(itemList);
        } catch (DbException e) {
            LogUtil.e(e);
        }
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
        }).initView().showDateDialog();
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
        }).initView().showDateDialog();
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
                    params.addBodyParameter(new String("attachments"), file);
                }
            }
        }
        HttpUtil.getInstance().sendInDialog(mContext, getString(R.string.txt_is_upload_data), ParamManager.parseBaseUrl("scheduleSave.action"), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                String result = responseInfo.result.toString();
                Schedule serverSchedule = GsonUtil.getInstance().getGson().fromJson(result, Schedule.class);
                try {
                    //复制文件到项目目录
                    schedule_aav.copyFile(serverSchedule.getAttachmentName());
                    DbOperationManager.getInstance().saveOrUpdate(serverSchedule);
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
        schedule.setAddress(address_et.getText().toString());
        schedule.setImportant(Integer.parseInt(important_sev.getValue().toString()));
        //设置类型
        ScheduleCategory category = new ScheduleCategory();
        category.setName(category_tv.getContent());
        if (category_tv.getValue() != null) {
            category.setId(category_tv.getValue() + "");
        }
        schedule.setCategory(category);
        schedule.setStartDate(startDate_sev.getValue().toString() + ":00");
        schedule.setEndDate(endDate_sev.getValue().toString() + ":00");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_FILE_CHOOSE:
                    Uri uri = data.getData();
                    String path = AndroidUtil.getPath(mContext, uri);
                    schedule_aav.addAttachment(path);
                    break;
            }
        }
    }

    @Override
    public void onFileChoose() {
        AndroidUtil.showFileChooser(this, CODE_FILE_CHOOSE);
    }
}
