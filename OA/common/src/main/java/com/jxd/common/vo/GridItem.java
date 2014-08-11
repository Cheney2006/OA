package com.jxd.common.vo;

import java.io.Serializable;

/**
 ****************************************************** 
 * @Description : GridView的item封装 
 * @Author : cy cy20061121@163.com
 * @Creation Date : 2013-5-22 下午8:59:03
 ****************************************************** 
 */
public class GridItem implements Serializable {
	private static final long serialVersionUID = -7456320445749039461L;
	private String title;
	private int icon;
	private Class cls;
	private int code;
	private int num;

	public GridItem() {
		super();
	}

	public GridItem(String title, int icon) {
		super();
		this.title = title;
		this.icon = icon;
	}

	public GridItem(String title, int icon, Class cls) {
		super();
		this.title = title;
		this.icon = icon;
		this.cls = cls;
	}

	public GridItem(String title, int icon, Class cls, int code) {
		super();
		this.title = title;
		this.icon = icon;
		this.cls = cls;
		this.code = code;
	}

	public GridItem(String title, int icon, Class cls, int code, int num) {
		super();
		this.title = title;
		this.icon = icon;
		this.cls = cls;
		this.code = code;
		this.num = num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Class getCls() {
		return cls;
	}

	public void setCls(Class cls) {
		this.cls = cls;
	}

}
