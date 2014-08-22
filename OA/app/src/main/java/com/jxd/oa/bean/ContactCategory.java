package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;

/**
 * *****************************************
 * Description ：通讯录分类
 * Created by cy on 2014/8/8.
 * *****************************************
 */
@Table(name = "t_contact_category")
public class ContactCategory extends AbstractBean {
    @Id(column = "id")
    @SerializedName("groupId")
    private String id;
    @Column(column = "groupNo")
    private int groupNo;
    @Column(column = "groupName")
    private String groupName;
    @Column(column = "userId")
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
