package com.jxd.common.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
/**
 * 
 ******************************************************
 *  @Description   : 自定义对话框类 
 *  @Author        : cy cy20061121@163.com
 *  @Creation Date : 2013-5-26 下午10:18:43 
 ******************************************************
 */
public class JxdAlertDialog implements OnClickListener{

	private Context context;
	private String title, message, positiveText, neutralText, negativeText;
	private AlertDialog alertDialog;

	public JxdAlertDialog(Context context, String title, String message) {
		this(context, title, message, "确定", null, null);
	}

    public JxdAlertDialog(Context context, String title, String message, String positiveText) {
        this(context, title, message, positiveText, null, null);
    }

    public JxdAlertDialog(Context context, String title, String message, String positiveText,String negativeText) {
        this(context, title, message, positiveText, null, negativeText);
    }

	public JxdAlertDialog(Context context, String title, String message, String positiveText, String neutralText, String negativeText) {
		this.context = context;
		this.title = title;
		this.message = message;
		this.positiveText = positiveText;
		this.neutralText = neutralText;
		this.negativeText = negativeText;
	}
	public void show() {
		if (alertDialog == null) {
			Builder builder = new Builder(context);
			builder.setTitle(title);
			builder.setMessage(message);
			builder.setCancelable(false);
			if (positiveText != null && !positiveText.trim().equals("")) {
				builder.setPositiveButton(positiveText, this);
			}
			if (neutralText != null && !neutralText.trim().equals("")) {
				builder.setNeutralButton(neutralText, this);
			}
			if (negativeText != null && !negativeText.trim().equals("")) {
				builder.setNegativeButton(negativeText, this);
			}
			alertDialog = builder.show();
		} else {
			alertDialog.show();
		}
	}

	protected void positive() {
		
	}

	protected void neutral() {
		
	}

	protected void negative() {
		
	}
	/**
	 * (非 Javadoc)  
	 * <p>Title: onClick</p>  
	 * <p>Description: </p>  
	 * @param dialog
	 * @param which  
	 * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)  
	 */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			positive();
			break;
		case DialogInterface.BUTTON_NEUTRAL:
			neutral();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			negative();
			break;
		}
	}
}
