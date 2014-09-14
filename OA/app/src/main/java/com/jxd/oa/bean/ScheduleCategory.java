package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;

/**
 * *****************************************
 * Description ：今日日程分类
 * Created by cy on 2014/8/8.
 * *****************************************
 */
@Table(name = "t_schedule_category")
public class ScheduleCategory extends AbstractBean {
    @Id(column = "id")
    @SerializedName("cgSid")
    private String id;
    @Column(column = "name")
    @SerializedName("cgName")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
