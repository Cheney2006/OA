package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Finder;
import com.yftools.db.annotation.Foreign;
import com.yftools.db.annotation.ForeignCollection;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;
import com.yftools.db.sqlite.FinderLazyLoader;
import com.yftools.db.sqlite.ForeignCollectionLazyLoader;
import com.yftools.db.sqlite.ForeignLazyLoader;

import java.util.List;

/**
 * *****************************************
 * Description ：电子邮件
 * Created by cy on 2014/8/6.
 * *****************************************
 */
@Table(name = "t_email")
public class Email extends AbstractBean {

    @Id(column = "id")
    private String id;
    @Column(column = "title")
    @SerializedName("subject")
    private String title;//标题
    @Column(column = "content")
    private String content;//内容
    @Column(column = "important")
    private String important;//重要性
    @Column(column = "formId")
    private String formId;//发送人

    @ForeignCollection(valueColumn = "id", foreign = "emailId", foreignAutoCreate = true)
    private ForeignCollectionLazyLoader<EmailRecipient> detailList;


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

    public ForeignCollectionLazyLoader<EmailRecipient> getDetailList() {
        return detailList;
    }

    public void setDetailList(ForeignCollectionLazyLoader<EmailRecipient> detailList) {
        this.detailList = detailList;
    }

    public String getImportant() {
        return important;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
