package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Finder;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;

import java.util.Date;

/**
 * *****************************************
 * Description ：通知公告
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
@Table(name = "t_notice")
public class Notice extends AbstractBean {
    @Id(column = "id")
    @SerializedName("notifyId")
    private String id;
    @Column(column = "title")
    @SerializedName("subject")
    private String title;//标题
    @Column(column = "content")
    private String content;//内容
    @Column(column = "createdUserId")
    @SerializedName("fromId")
    private String createdUserId;
    @Finder(valueColumn = "createdUserId", targetColumn = "id")
    private User createdUser;//发布人.//因为后台没有用对象。所以这里就直接查一下
    @Column(column = "publishTime")
    @SerializedName("sendTime")
    private Date publishTime;//发布时间
    @Column(column = "attachmentName")
    private String attachmentName;//邮件列表，XX.doc|xx.xls
    @Column(column = "attachmentSize")
    private String attachmentSize;//邮件大小,10923|23432
    @Column(column = "isRead")
    private boolean isRead;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getCreatedUser() {
        return createdUser;
    }

    public String getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentSize() {
        return attachmentSize;
    }

    public void setAttachmentSize(String attachmentSize) {
        this.attachmentSize = attachmentSize;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
}
