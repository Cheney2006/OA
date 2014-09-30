package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Table;
import com.yftools.db.annotation.Transient;

import java.util.Date;

/**
 * *****************************************
 * Description ：消息中心
 * Created by cy on 2014/8/6.
 * *****************************************
 */
@Table(name = "t_message")
public class Message extends AbstractBean {

    private static final String COLUMN_ID = "id";
    //@Column(column = COLUMN_ID)
    private int id;
    private String title;//标题
    private String beanId;//数据ID
    private String beanName;//实体名
    private int type;//类型
    private Date createdDate;//创建时间
    private boolean isRead;//是否已读
    private String operation;//操作
    @Transient
    private String beanData;
    @Transient
    @SerializedName("id")
    private int version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getBeanData() {
        return beanData;
    }

    public void setBeanData(String beanData) {
        this.beanData = beanData;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
