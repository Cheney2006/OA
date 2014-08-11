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
 * Description ：部门
 * Created by cy on 2014/8/4.
 * *****************************************
 */
@Table(name = "t_department")
public class Department extends AbstractBean {

    @Id(column = "id")
    @SerializedName("deptId")
    private String id;
    @Column(column = "parentId")
    @SerializedName("parentDept")
    private int parentId;
    @Column(column = "deptName")
    private String deptName;
    @Column(column = "deptCode")
    private String deptCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }
}
