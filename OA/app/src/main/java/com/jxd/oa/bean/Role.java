package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.NoAutoIncrement;
import com.yftools.db.annotation.Table;
import com.yftools.db.annotation.Unique;

/**
 * *****************************************
 * Description ：角色
 * Created by cy on 2014/8/4.
 * *****************************************
 */
@Table(name = "t_role")
public class Role extends AbstractBean {

    @Id(column = "id")
    @SerializedName("privId")
    private String id;
    @Column(column = "roleNo")
    @SerializedName("privNo")
    private int roleNo;
    @Column(column = "name")
    @SerializedName("privName")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRoleNo() {
        return roleNo;
    }

    public void setRoleNo(int roleNo) {
        this.roleNo = roleNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
