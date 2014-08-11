package com.jxd.common.vo;

import java.io.Serializable;

public class BottomMenuItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;
	private int iconNormal;
	private int iconSelected;
	private String id;
	private Class cls;
	private boolean isIncluded=true;


	public BottomMenuItem(String title, int iconNormal, Class cls) {
		super();
		this.title = title;
		this.iconNormal = iconNormal;
		this.id = cls.getSimpleName();
		this.cls = cls;
	}

	public BottomMenuItem(String title, int iconNormal, int iconSelected, Class cls) {
		super();
		this.title = title;
		this.iconNormal = iconNormal;
		this.iconSelected = iconSelected;
		this.id = cls.getSimpleName();
		this.cls = cls;
	}

	public BottomMenuItem(String title, int iconNormal, int iconSelected, Class cls,boolean isIncluded) {
		super();
		this.title = title;
		this.iconNormal = iconNormal;
		this.iconSelected = iconSelected;
		this.id = cls.getSimpleName();
		this.cls = cls;
		this.isIncluded=isIncluded;
	}

	public BottomMenuItem(String title, int iconNormal, int iconSelected, String id, Class cls,boolean isIncluded) {
		super();
		this.title = title;
		this.iconNormal = iconNormal;
		this.iconSelected = iconSelected;
		this.id = id;
		this.cls = cls;
		this.isIncluded=isIncluded;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getIconNormal() {
		return iconNormal;
	}

	public void setIconNormal(int iconNormal) {
		this.iconNormal = iconNormal;
	}

	public int getIconSelected() {
		return iconSelected;
	}

	public void setIconSelected(int iconSelected) {
		this.iconSelected = iconSelected;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Class getCls() {
		return cls;
	}

	public void setCls(Class cls) {
		this.cls = cls;
	}

	public boolean isIncluded() {
		return isIncluded;
	}

	public void setIncluded(boolean isIncluded) {
		this.isIncluded = isIncluded;
	}
	
}
