package com.jxd.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jxd.common.R;

/**
 ****************************************************** 
 * @Description : 自定义的textview 
 * @Author : cy cy20061121@163.com
 * @Creation Date : 2013-5-26 下午10:19:39
 ****************************************************** 
 */
public class SelectedTextView extends TextView {

	private String value = "";

	public SelectedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		/* 这里取得declare-styleable集合 */
		TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.MyView);
		/* 这里从集合里取出相对应的属性值,第二参数是如果使用者没用配置该属性时所用的默认值 */
		value = typeArray.getString(R.styleable.MyView_value);
		/* 关闭资源 */
		typeArray.recycle();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		//this.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

}
