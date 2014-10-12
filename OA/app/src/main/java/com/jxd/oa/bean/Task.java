package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Foreign;
import com.yftools.db.annotation.Table;

/**
 * *****************************************
 * Description ：我的工作
 * Created by cy on 2014/9/14.
 * *****************************************
 */
@Table(name = "t_task")
public class Task extends AbstractBean {
    @SerializedName("tid")
    private String id;
    private String title;//主题
    private String content;//内容
    private int important;//重要性
    private String principal; // 负责人
    private String participant; // 参与人
    @SerializedName("taskCategory")
    @Foreign(column = "categoryId", foreign = "id", foreignAutoCreate = true)
    private TaskCategory category;//类型 @SerializedName("taskCategory"),@Foreign加了这个就不行
    @SerializedName("btime")
    private String startDate;//开始时间
    @SerializedName("etime")
    private String endDate;//结束时间
    @SerializedName("file")
    private String attachmentName;//附件列表，XX.doc|xx.xls
    private String attachmentSize;//附件大小,10923|23432
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }
}
