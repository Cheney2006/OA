package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;

/**
 * *****************************************
 * Description ：邮件接收人
 * Created by cywf on 2014/8/10.
 * *****************************************
 */
@Table(name = "t_email_recipient")
public class EmailRecipient extends AbstractBean {
    @Id(column = "id")
    private String id;
    @Column(column = "toId")
    private String toId;
    @Column(column = "readTime")
    private String readTime;
    @Column(column = "emailId")
    @SerializedName("bodyId")
    private String emailId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
