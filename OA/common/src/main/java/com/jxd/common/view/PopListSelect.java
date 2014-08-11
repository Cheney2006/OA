package com.jxd.common.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jxd.common.R;
import com.jxd.common.adapter.PopListAdapter;
import com.jxd.common.vo.Item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 ******************************************************
 *  @Description   : popwindow实现下拉列表，在api 11后有一个ListPopWindow，直接可以继承 
 *  @Author        : cy cy20061121@163.com
 *  @Creation Date : 2013-5-26 下午10:19:26 
 ******************************************************
 */
public class PopListSelect extends PopupWindow {

	private List<Item> datas;
	private int postion = 0;

	public PopListSelect(Context context, List<Item> datas, OnItemClickListener itemClickEvent, View view) {
		super(context);
		initPop(context, datas, itemClickEvent, view);
	}

	public PopListSelect(Context context, List<Item> datas, View view, SelectedTextView selected) {
		super(context);
		initPop(context, datas, new ItemClickEvent(selected), view);
		initSelectedPosition(selected);
	}
	/**
	 * 初始化popWindow
	 * @param context
	 * @param datas
	 * @param itemClickEvent
	 * @param view
	 */
	private void initPop(Context context, List<Item> datas, OnItemClickListener itemClickEvent, View view) {
		this.datas = datas;
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewGroup menuView = (ViewGroup) inflater.inflate(R.layout.pop_list, null, true);
		ListView list = (ListView) menuView.findViewById(R.id.pop_list);
		list.setAdapter(new PopListAdapter(context, datas));
		list.setSelection(postion);// 默认选中位置，这里可以根据selectedTextView中的值来。。
		list.requestFocus();
		//list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list.setOnItemClickListener(itemClickEvent);
		// list.setOnItemClickListener(new itemClickEvent());

		this.setContentView(menuView);
		this.setWidth(view.getWidth());
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// popupWindow+listview,实现listview选中事件和pop点击隐藏的效果，必须加这行代码。
		this.setBackgroundDrawable(new BitmapDrawable());
		if (context!=null&&view!=null) {
			this.showAsDropDown(view);
		}
		//如果为false点击相关的空间表面上没有反应，但事件是可以监听到的。   
        //listview的话就没有了作用。
		this.setFocusable(true);
		this.update();
	}
	/**
	 * 初始化默认选中位置
	 * @param selected
	 */
	private void initSelectedPosition(SelectedTextView selected) {
		if (!"".equals(selected.getValue())) {
			for (int i = 0, len = datas.size(); i < len; i++) {
				Item item = datas.get(i);
				if (item.getText().equals(selected.getText().toString()) && item.getValue().equals(selected.getValue())) {
					postion = i;
				}
			}
		}
	}

	private class ItemClickEvent implements OnItemClickListener {

		SelectedTextView selected;

		public ItemClickEvent(SelectedTextView selected) {
			super();
			this.selected = selected;
		}

		public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
			Item item = datas.get(position);
			selected.setValue(item.getValue() + "");
			selected.setText(item.getText());
			if (isShowing()) {
				dismiss();
			}
		}
	}

	public static <T> List<Item> transDataToListItem(List<T> datas) {
		List<Item> items = new ArrayList<Item>();
		if (datas != null) {
			Method metd = null;
			Object value = null;
			for (T data : datas) {
				Item item = new Item();
				Class cls = data.getClass();
				try {
					metd = cls.getMethod("getId", new Class[]{});
					value = metd.invoke(data, new Object[]{});
					item.setValue(value);
					metd = cls.getMethod("getName", new Class[]{});
					value = metd.invoke(data, new Object[]{});
					item.setText(value + "");
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				// System.out.println(item);
				items.add(item);
			}
		}
		return items;
	}

	public <T> List<Item> transDataToListItem(List<T> datas, String[] fields) {
		List<Item> items = new ArrayList<Item>();
		String[] flds=new String[2]; 
		if(fields==null){
			flds[0]="id";
			flds[1]="name";
		}else{
			if(fields.length==1){
				flds[0]="id";
				flds[1]=fields[0];
			}else if(fields.length>1){
				flds[0]=fields[0];
				flds[1]=fields[1];
			}
		}
		if (datas != null) {
			Method metd = null;
			Object value = null;
			for (T data : datas) {
				Item item = new Item();
				Class cls = data.getClass();
				try {
					//for (String filed : fields) {
						metd = cls.getMethod("get" + change(flds[0]), new Class[]{});
						value = metd.invoke(data, new Object[]{});
						item.setValue(value);
						metd = cls.getMethod("get"+ change(flds[1]), new Class[]{});
						value = metd.invoke(data, new Object[]{});
						item.setText(value + "");
					//}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				items.add(item);
			}
		}
		return items;
	}

    /**
     * @param src
     *            源字符串
     * @return 字符串，将src的第一个字母转换为大写，src为空时返回null
     */
    public static String change(String src) {
        if (src != null) {
            StringBuffer sb = new StringBuffer(src);
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        } else {
            return null;
        }
    }

}
