package com.jxd.oa.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.Schedule;
import com.jxd.oa.constants.Const;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.view.AttachmentViewView;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.exception.DbException;
import com.yftools.util.DateUtil;
import com.yftools.view.annotation.ViewInject;

/**
 * *****************************************
 * Description ：日程详情
 * Created by cy on 2014/8/18.
 * *****************************************
 */
public class ScheduleDetailActivity extends AbstractActivity {

    @ViewInject(R.id.title_tv)
    private TextView title_tv;
    @ViewInject(R.id.important_tv)
    private TextView important_tv;
    @ViewInject(R.id.category_tv)
    private TextView category_tv;
    @ViewInject(R.id.starDate_tv)
    private TextView starDate_tv;
    @ViewInject(R.id.endDate_tv)
    private TextView endDate_tv;
    @ViewInject(R.id.address_tv)
    private TextView address_tv;
    @ViewInject(R.id.content_tv)
    private TextView content_tv;
    @ViewInject(R.id.attachment_label)
    private TextView attachment_label;
    @ViewInject(R.id.schedule_avv)
    private AttachmentViewView schedule_avv;
    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getString(R.string.txt_title_schedule_detail));
        schedule = (Schedule) getIntent().getSerializableExtra("schedule");
        initData();
    }

    public void initData() {
        title_tv.setText(schedule.getTitle());
        important_tv.setText(Const.getName("TYPE_IMPORTANT_", schedule.getImportant()));
        category_tv.setText(schedule.getCategory().getName());
        starDate_tv.setText(DateUtil.dateToString("yyyy-MM-dd HH:mm", schedule.getStartDate()));
        endDate_tv.setText(DateUtil.dateToString("yyyy-MM-dd HH:mm", schedule.getEndDate()));
        address_tv.setText(schedule.getAddress());
        content_tv.setText(schedule.getContent());
        if (!TextUtils.isEmpty(schedule.getAttachmentName()) && !TextUtils.isEmpty(schedule.getAttachmentSize())) {
            attachment_label.setVisibility(View.VISIBLE);
            schedule_avv.setVisibility(View.VISIBLE);
            schedule_avv.initData(schedule.getAttachmentName(), schedule.getAttachmentSize());
        } else {
            attachment_label.setVisibility(View.GONE);
            schedule_avv.setVisibility(View.GONE);
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
                new JxdAlertDialog(this, getString(R.string.txt_tips), "确定设为已完成吗？", getString(R.string.txt_confirm), getString(R.string.txt_cancel)) {
                    @Override
                    protected void positive() {
                        try {
                            schedule.setFinished(true);
                            DbOperationManager.getInstance().save(schedule);
                            setResult(RESULT_OK);
                            finish();
                        } catch (DbException e) {
                            LogUtil.e(e);
                        }
                    }
                }.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
