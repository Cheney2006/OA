package com.jxd.oa.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Finder;
import com.yftools.db.annotation.Foreign;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;
import com.yftools.db.annotation.Transient;
import com.yftools.db.sqlite.ForeignLazyLoader;

import java.util.Date;

/**
 * *****************************************
 * Description ：用户
 * Created by cy on 2014/7/29.
 * *****************************************
 */
@Table(name = "t_user")
public class User extends AbstractBean {

    @Id(column = "id")
    private String id;
    @Column(column = "photo")
    @SerializedName("photos")
    private byte[] photo;
    @Column(column = "birthday")
    private Date birthday;
    @Column(column = "username")
    @SerializedName("userId")
    private String username;
    @Column(column = "name")
    @SerializedName("userName")
    private String name;
    @Column(column = "password")
    private String password;
    @Column(column = "sex")
    private String sex;
    //@Foreign(column = "departmentId", foreign = "id")
    @Column(column = "departmentId")
    @SerializedName("deptId")
    private String departmentId;
    @Finder(valueColumn = "departmentId", targetColumn = "id")
    private Department department;
    //@Foreign(column = "roleId", foreign = "id")
    @Column(column = "roleId")
    @SerializedName("privId")
    private String roleId;
    @Finder(valueColumn = "roleId", targetColumn = "id")
    private Role role;
    @Transient
    public Date loginTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
