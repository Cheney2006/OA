package com.jxd.oa.service;

import android.app.IntentService;
import android.content.Intent;

import com.jxd.oa.bean.Message;
import com.jxd.oa.bean.base.AbstractBean;
import com.jxd.oa.constants.Constant;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.jxd.oa.utils.ParamManager;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.http.RequestParams;
import com.yftools.http.ResponseStream;
import com.yftools.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * *****************************************
 * Description ：增量更新、推送时，请求数据
 * 一个intentService可以处理多个任务，只不过是一个接着一个的顺序来处理的
 * Created by cy on 2014/8/6.
 * *****************************************
 */
public class IncrementUpdateService extends IntentService {

    public IncrementUpdateService() {
        super("IncrementUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            int version = intent.getIntExtra("version", SysConfig.getInstance().getMaxVersion());
            if (version > SysConfig.getInstance().getMaxVersion()) {
                RequestParams params = ParamManager.setDefaultParams();
                params.addBodyParameter("id", SysConfig.getInstance().getMaxVersion() + "");
                ResponseStream result = HttpUtil.getInstance().sendSync(ParamManager.parseBaseUrl("versionList.action"), params);
                String resultStr = result.readString();
                JSONObject jsonObject = new JSONObject(resultStr);
                if (jsonObject.getBoolean("success")) {
                    //一个一个的保存。因为要保存每一个的版本号。不用事务，成功一个是一个。
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Message message;
                    if (jsonArray != null && jsonArray.length() > 0) {
                        for (int i = 0, len = jsonArray.length(); i < len; i++) {
                            jsonObject = jsonArray.getJSONObject(i);
                            message = new Message();
                            message.setTitle(jsonObject.getString("title"));
                            message.setBeanId(jsonObject.getString("beanId"));
                            message.setBeanName(jsonObject.getString("beanName"));
                            message.setOperation(jsonObject.getString("operation"));
                            message.setCreatedDate(DateUtil.stringToDateTime(jsonObject.getString("createdDate")));
                            message.setType(jsonObject.getInt("type"));
                            String beanData = jsonObject.getString("beanData");
                            String clazzName = "com.jxd.oa.bean." + message.getBeanName();
                            Class clazz = Class.forName(clazzName).newInstance().getClass();
                            AbstractBean bean = (AbstractBean) GsonUtil.getInstance().getGson().fromJson(beanData, clazz);
                            DbOperationManager.getInstance().saveOrUpdate(bean);
                            DbOperationManager.getInstance().saveOrUpdate(message);
                            SysConfig.getInstance().setMaxVersion(jsonObject.getInt("id"));
                        }
                        sendBroadcast(new Intent(Constant.ACTION_REFRESH));
                    }
                } else {
                    throw new Exception(jsonObject.getString("message"));
                }
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }
}
