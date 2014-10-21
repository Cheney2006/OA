package com.jxd.oa.constants;

import com.jxd.common.vo.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作常量
 */
public enum Const {
    // 表名跟实体类
    TYPE_MESSAGE_WORK("工作信息", 1), TYPE_MESSAGE_OPERATION_REMIND("操作提醒", 2), TYPE_MESSAGE_TIPS("提示信息", 3), TYPE_MESSAGE_BASE("基本信息", 4),
    STATUS_ADDRESS_TODO("待采集", 1), STATUS_ADDRESS_SUBMIT("提交采集", 2), STATUS_ADDRESS_FINISHED("完成采集", 3), TYPE_MESSAGE_ABANDON("废弃采集", 4),
    TYPE_IMPORTANT_LOW("一般", 0), TYPE_IMPORTANT_MIDDLE("重要", 1), TYPE_IMPORTANT_HIGH("非常重要", 2),
    STATUS_BEING("待办", 1), STATUS_PASS("同意", 2), STATUS_REFUSE("拒绝", 3),
    TYPE_LEAVE_AFFAIRS("事假", 1), TYPE_LEAVE_ILLNESS("病假", 2), TYPE_LEAVE_ANNUAL("年假", 3), TYPE_LEAVE_OTHER("其它", 4),
    TYPE_EXPENSE_FARE("车费", 1), TYPE_EXPENSE_MEAL("餐费", 2), TYPE_EXPENSE_HOTEL("住宿费", 3), TYPE_EXPENSE_OTHER("其它", 4),
    TYPE_TODO_LEAVE_APPLICATION("请假单", 1), TYPE_TODO_EXPENSE_ACCOUNT("报销单", 2),
    TYPE_SIGN_IN("签到", 1), TYPE_SIGN_OUT("签退", 2),
    COOR_TYPE_GCJ("国测局", "gcj02"), COOR_TYPE_BD("百度", "bd09ll"),
    SEX_FEMALE("男", 0), SEX_MALE("女", 1);
    // 成员变量
    private String name;
    private Object value;

    // 构造方法
    private Const(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    // 普通方法
    public static String getName(Object value) {
        for (Const c : Const.values()) {
            if (c.getValue().toString().equals(value)) {
                return c.getName();
            }
        }
        return null;
    }

    // 普通方法
    public static String getName(String prefix, Object value) {
        for (Const c : Const.values()) {
            if (c.name().startsWith(prefix) && c.getValue().toString().trim().equals(value.toString().trim())) {
                return c.getName();
            }
        }
        return null;
    }

    public static List<String> getNameList(String prefix) {
        List<String> list = new ArrayList<String>();
        for (Const c : Const.values()) {
            if (c.name().startsWith(prefix)) {
                list.add(c.getName());
            }
        }
        return list;
    }

    public static List<Object> getValueList(String prefix) {
        List<Object> list = new ArrayList<Object>();
        for (Const c : Const.values()) {
            if (c.name().startsWith(prefix)) {
                list.add(c.getValue());
            }
        }
        return list;
    }

    /**
     * 按照前缀取得键值对
     *
     * @param prefix
     * @return
     */
    public static List<Item> getList(String prefix) {
        List<Item> items = new ArrayList<Item>();
        for (Const c : Const.values()) {
            // System.out.println("c.name()="+c.name());
            if (c.name().startsWith(prefix)) {
                // hm.put(c.getValue(), c.getName());
                items.add(new Item(c.getName(), (Integer) c.getValue()));
            }
        }
        return items;
    }

    /**
     * 按照前缀取得键值对
     *
     * @param prefix
     * @return
     */
    public static Map<Object, String> getMap(String prefix) {
        HashMap<Object, String> hm = new HashMap<Object, String>();
        for (Const c : Const.values()) {
            // System.out.println("c.name()="+c.name());
            if (c.name().startsWith(prefix)) {
                hm.put(c.getValue(), c.getName());
            }
        }
        return hm;
    }

    // get set_pressed 方法

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 覆盖方法
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name + "_" + this.value;
    }
}
