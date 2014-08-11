package com.jxd.common.asynctask;

import com.yftools.json.Result;

/**  
 ******************************************************
 *  @Description   : 异步任务回调接口
 *  @Author        : cy cy20061121@163.com
 *  @Creation Date : 2013-5-22 下午8:43:14 
 ******************************************************
 */
public interface TaskFinishedCallBack {
	void onFinished(Result result);
}
