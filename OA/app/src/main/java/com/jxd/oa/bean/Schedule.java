package com.jxd.oa.bean;

import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;

import java.util.Date;

/**
 * *****************************************
 * Description ：今日日程
 * Created by cy on 2014/8/18.
 * *****************************************
 */
@Table(name = "t_schedule")
public class Schedule extends AbstractBean{
    @Id(column = "id")
    private String id;
    @Column(column = "title")
    private String title;//主题
    @Column(column = "address")
    private String address;//地点
    @Column(column = "content")
    private String content;//内容
    @Column(column = "important")
    private String important;//重要性
    @Column(column = "type")
    private String type;//类型
    @Column(column = "startData")
    private Date startData;//开始时间
    @Column(column = "endData")
    private Date endData;//结束时间
    @Column(column = "attachmentName")
    private String attachmentName;//邮件列表，XX.doc|xx.xls
    @Column(column = "attachmentSize")
    private String attachmentSize;//邮件大小,10923|23432
    private boolean isFinished;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImportant() {
        return important;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStartData() {
        return startData;
    }

    public void setStartData(Date startData) {
        this.startData = startData;
    }

    public Date getEndData() {
        return endData;
    }

    public void setEndData(Date endData) {
        this.endData = endData;
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

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isRead) {
        this.isFinished = isRead;
    }
}
