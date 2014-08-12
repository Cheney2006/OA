package com.jxd.oa.bean.base;

import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Id;

import java.io.Serializable;

/**
 * *****************************************
 * Description ：基类bean
 * Created by cy on 2014/7/29.
 * *****************************************
 */
public abstract class AbstractBean implements Serializable, Cloneable {

    //@Id // 如果主键没有命名名为id或_id的时，需要为主键添加此注解
    //@NoAutoIncrement // int,long类型的id默认自增，不想使用自增时添加此注解
//    @Id(column = "mid")
//    private int mid;//假如本地用自增ID作为主键的话，就会出现更新不能根据主键来更新的情况，所以还是用服务器的ID作为主键
//
//    public int getMid() {
//        return mid;
//    }
//
//    public void setMid(int mid) {
//        this.mid = mid;
//    }
}
