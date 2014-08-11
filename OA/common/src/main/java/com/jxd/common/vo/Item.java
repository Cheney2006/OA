package com.jxd.common.vo;

/**  
 ******************************************************
 *  @Description   : 下拉的item 
 *  @Author        : cy cy20061121@163.com
 *  @Creation Date : 2013-5-22 下午8:59:16 
 ******************************************************
 */
public class Item {
	private String text = "";
	private Object value;

	
	public Item() {
		super();
	}


	public Item(String text, Object value) {
		super();
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return this.text;
	}

	
}
