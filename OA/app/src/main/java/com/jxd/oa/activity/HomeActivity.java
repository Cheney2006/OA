package com.jxd.oa.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jxd.common.view.JxdAlertDialog;
import com.jxd.oa.R;
import com.jxd.oa.activity.base.SelectImageActivity;
import com.jxd.oa.bean.EmailRecipient;
import com.jxd.oa.service.IncrementUpdateService;
import com.jxd.oa.bean.User;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.ParamManager;
import com.yftools.BitmapUtil;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.ViewUtil;
import com.yftools.bitmap.BitmapCommonUtil;
import com.yftools.db.sqlite.Selector;
import com.yftools.exception.DbException;
import com.yftools.exception.HttpException;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseInfo;
import com.yftools.http.callback.RequestCallBack;
import com.yftools.json.Json;
import com.yftools.util.AndroidUtil;
import com.yftools.util.BitmapDecodeUtil;
import com.yftools.util.UUIDGenerator;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

import java.io.File;

/**
 * *****************************************
 * Description ：首页
 * Created by cy on 2014/7/29.
 * *****************************************
 */
public class HomeActivity extends SelectImageActivity {

    public static final int BACK_DELAY_MILLIS = 2000;
    @ViewInject(R.id.name_tv)
    private TextView name_tv;
    @ViewInject(R.id.role_tv)
    private TextView role_tv;
    @ViewInject(R.id.department_tv)
    private TextView department_tv;
    @ViewInject(R.id.photo_iv)
    private ImageView photo_iv;
    @ViewInject(R.id.info_rl)
    private RelativeLayout info_rl;
    @ViewInject(R.id.emailNum_tv)
    private TextView emailNum_tv;
    private long mExitTime;
    private Uri imageUri;//The Uri to store the big bitmap，跟输入的区分出来。不然在选择图片裁剪，把原有的图片也裁剪小了。
    private File photo;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ViewUtil.inject(this);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        initData();
        checkSync();
        initNum();
    }

    private void initData() {
        registerForContextMenu(info_rl);
        try {
            user = DbOperationManager.getInstance().getBeanById(User.class, SysConfig.getInstance().getUserId());
            if (user.getPhoto() != null) {
                photo_iv.setImageBitmap(BitmapCommonUtil.toRoundCorner(BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length), AndroidUtil.dip2px(mContext, Constant.ROUND_CORNER)));
            }
            name_tv.setText(user.getName());
            if (user.getDepartment() != null) {
                department_tv.setText(user.getDepartment().getDeptName());
            }
            if (user.getRole() != null) {
                role_tv.setText(user.getRole().getName());
            }
        } catch (DbException e) {
            displayToast(e.getMessage());
            LogUtil.e(e);
        }
    }

    private void checkSync() {
        if (SysConfig.getInstance().getSyncFinished()) {
            //取服务器最大版本号
            getMaxVersion();
        } else {
            new JxdAlertDialog(mContext, "提示", "是否同步数据？", getResources().getString(R.string.txt_confirm), getResources().getString(R.string.txt_cancel)) {
                @Override
                protected void positive() {
                    startActivity(new Intent(mContext, SyncDataActivity.class));
                }

                @Override
                protected void negative() {
                    exitApp();
                }
            }.show();
        }
    }

    private void getMaxVersion() {
        HttpUtil.getInstance().send(ParamManager.parseBaseUrl("getMaxVersionId.action"), ParamManager.setDefaultParams(), new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                //增量更新、推送数据的处理、请求可以使用IntentService
                Intent serviceIntent = new Intent(mContext, IncrementUpdateService.class);
                serviceIntent.putExtra("version", responseInfo.result.getInt("version"));
                startService(serviceIntent);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
            }
        });
    }

    @OnClick(R.id.info_rl)
    public void photoClick(View view) {
        view.showContextMenu();
    }

    @OnClick(R.id.email_ll)
    public void emailClick(View view) {
        startActivity(new Intent(mContext, EmailActivity.class));
    }

    @OnClick(R.id.notice_ll)
    public void noticeClick(View view) {
        startActivity(new Intent(mContext, NoticeActivity.class));
    }

    @OnClick(R.id.schedule_ll)
    public void scheduleClick(View view) {
//        startActivity(new Intent(mContext, ScheduleActivity.class));
    }

    @OnClick(R.id.myTask_ll)
    public void myTaskClick(View view) {
//        startActivity(new Intent(mContext, AddressCollectActivity.class));
    }

    @OnClick(R.id.contacts_ll)
    public void contactsClick(View view) {
        startActivity(new Intent(mContext, ContactsActivity.class));
    }

    @OnClick(R.id.cloud_ll)
    public void cloudClick(View view) {
//        startActivity(new Intent(mContext, CloudActivity.class));
    }

    @OnClick(R.id.sign_ll)
    public void signClick(View view) {
//        startActivity(new Intent(mContext, SignAddressActivity.class));
    }

    @OnClick(R.id.clearData_btn)
    public void clearDataClick(View view) {
        new JxdAlertDialog(this, getResources().getString(R.string.txt_tips), "确定清空数据？", getResources().getString(R.string.txt_confirm), getResources().getString(R.string.txt_cancel)) {
            @Override
            protected void positive() {
                try {
                    DbOperationManager.getInstance().clearDb();
                    SysConfig.getInstance().clearData();
                    exitApp();
                    startActivity(new Intent(mContext, LoginActivity.class));
                } catch (DbException e) {
                    LogUtil.e(e);
                }
            }

        }.show();
    }

    @OnClick(R.id.exit_btn)
    public void exitClick(View view) {
        new JxdAlertDialog(this, getResources().getString(R.string.txt_tips), "确定退出？", getResources().getString(R.string.txt_confirm), getResources().getString(R.string.txt_cancel)) {
            @Override
            protected void positive() {
                exitApp();
            }

        }.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > BACK_DELAY_MILLIS) {
                displayToast("再按一次返回桌面");
                mExitTime = System.currentTimeMillis();
            } else {
                moveTaskToBack(false);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case CAPTURE_REQUEST:
                // 设置文件保存路径这里放在跟目录下
                photo = new File(BitmapUtil.getInstance(mContext).getCachePath(), fileName);
                imageUri = Uri.fromFile(photo);
                cropImageUri(imageUri, imageUri);
                break;
            case PHOTO_REQUEST:// 读取相册缩放图片
                if (data != null) {
                    Uri in = data.getData();
                    photo = new File(BitmapUtil.getInstance(mContext).getCachePath(), UUIDGenerator.getUUID() + ".jpg");
                    imageUri = Uri.fromFile(photo);
                    cropImageUri(in, imageUri);
                }
                break;
            case PHOTORESOULT_REQUEST:// 处理结果
                if (data != null) {
                    if (imageUri != null) {
                        Bitmap bitmap = decodeUriAsBitmap(imageUri);//大图处理
                        uploadImage(BitmapDecodeUtil.getInstance().Bitmap2Bytes(bitmap));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void uploadImage(final byte[] photos) {
        RequestParams params = ParamManager.setDefaultParams();
        params.addBodyParameter("photo", photos, "photo.jpg");
        HttpUtil.getInstance().sendInDialog(mContext, "正在上传图像...", ParamManager.parseBaseUrl("updateImage.action"), params, new RequestCallBack<Json>() {
            @Override
            public void onSuccess(ResponseInfo<Json> responseInfo) {
                photo_iv.setImageBitmap(BitmapCommonUtil.toRoundCorner(BitmapFactory.decodeByteArray(photos, 0, photos.length), AndroidUtil.dip2px(mContext, Constant.ROUND_CORNER)));
                //保存到本地数据库
                if (user != null) {
                    user.setPhoto(photos);
                    try {
                        DbOperationManager.getInstance().save(user);
                    } catch (DbException e) {
                        LogUtil.d(e);
                        displayToast("保存图像失败：" + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                displayToast(msg);
            }
        });
    }

    @Override
    protected void refreshData() {
        initNum();
    }

    private void initNum() {
        //电子邮件未读数
        try {
            long emailNum = DbOperationManager.getInstance().count(Selector.from(EmailRecipient.class).where("toId", "=", SysConfig.getInstance().getUserId()).and("readTime", "=", ""));
            if (emailNum > 0) {
                emailNum_tv.setVisibility(View.VISIBLE);
                emailNum_tv.setText(emailNum + "");
            } else {
                emailNum_tv.setVisibility(View.GONE);
            }
        } catch (DbException e) {
            LogUtil.e(e);
        }
    }
}
