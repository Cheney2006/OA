package com.jxd.oa.asynctask;

import android.content.Context;
import android.os.Bundle;

import com.jxd.oa.bean.Message;
import com.jxd.oa.bean.base.AbstractBean;
import com.jxd.oa.constants.SysConfig;
import com.jxd.oa.utils.DbOperationManager;
import com.jxd.oa.utils.GsonUtil;
import com.yftools.HttpUtil;
import com.yftools.LogUtil;
import com.yftools.http.ResponseStream;
import com.yftools.task.PriorityAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * *****************************************
 * Description ：增量更新
 * Created by cy on 2014/7/30.
 * *****************************************
 */
public class IncrementUpdateTask extends PriorityAsyncTask<Integer, String, Bundle> {


    private TaskFinishedCallBack callBack;


    public IncrementUpdateTask(Context context, TaskFinishedCallBack callBack) {
        super();
        this.callBack = callBack;
    }

    @Override
    protected Bundle doInBackground(Integer... params) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("success", true);
        try {
            incrementUpdateData();
        } catch (Exception e) {
            LogUtil.e(e);
            bundle.putBoolean("success", false);
            bundle.putString("message", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bundle result) {
        callBack.onFinished(result);
    }

    private <T extends AbstractBean> void incrementUpdateData() throws Exception {
//        ResponseStream result = HttpUtil.getInstance().sendSync("Action!findAll");
//        String resultStr = result.readString();
        String resultStr = "{\"success\":true,\"data\"=[\n" +
                "{\"beanId\":1130,beanName:\"Department\",\"version\":81,\"operation\":\"EDIT\",\"beanData\":{\"deptCode\": \"0505\",\"deptId\": 1130,\"deptName\": \"研发部\",\"parentDept\": 1024}},\n" +
                "{\"beanId\":25,beanName:\"Role\",\"version\":82,\"operation\":\"ADD\",\"beanData\":{\"funcIdStr\": \"\",\"privId\": 25,\"privName\": \"经理\",\"privNo\": 7}}\n" +
                "],\"message\":\"\"}";
        JSONObject jsonObject = new JSONObject(resultStr);
        if (jsonObject.getBoolean("success")) {
//            Type type = new com.google.gson.reflect.TypeToken<List<Message>>() {
//            }.getType();
//            List<Message> messageList = GsonUtil.getInstance().getGson().fromJson(jsonObject.getString("data"), type);
//            for (Message message : messageList) {
//                String clazzName = "com.jxd.oa.bean." + message.getBeanName();
//                Class clazz = Class.forName(clazzName).newInstance().getClass();
//                AbstractBean bean = (AbstractBean) GsonUtil.getInstance().getGson().fromJson(message.getBeanData(), clazz);
//                DbOperationManager.getInstance().save(bean);
//                SysConfig.getInstance().setMaxVersion(message.getVersion());
//            }
//            DbOperationManager.getInstance().save(messageList);


            JSONArray jsonArray = jsonObject.getJSONArray("data");
            Message message;
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0, len = jsonArray.length(); i < len; i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    message=new Message();
                    message.setBeanId(jsonObject.getString("beanId"));
                    message.setBeanName(jsonObject.getString("beanName"));
                    message.setOperation(jsonObject.getString("operation"));
                    String beanData=jsonObject.getString("beanData");
                    String clazzName = "com.jxd.oa.bean." + message.getBeanName();
                    Class clazz = Class.forName(clazzName).newInstance().getClass();
                    AbstractBean bean = (AbstractBean) GsonUtil.getInstance().getGson().fromJson(beanData, clazz);
                    DbOperationManager.getInstance().save(bean);
                    SysConfig.getInstance().setMaxVersion(jsonObject.getInt("version"));
                }
            }
        } else {
            throw new Exception(jsonObject.getString("message"));
        }
    }

    public interface TaskFinishedCallBack {
        void onFinished(Bundle result);
    }
}
