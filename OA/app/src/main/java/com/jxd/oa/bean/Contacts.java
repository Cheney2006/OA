package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;

/**
 * *****************************************
 * Description ：通讯录
 * Created by cy on 2014/8/8.
 * *****************************************
 */
@Table(name = "t_contacts")
public class Contacts extends AbstractBean {
    @Id(column = "id")
    @SerializedName("addressId")
    private String id;
    @Column(column = "userId")
    private String userId;
    @Column(column = "categoryId")
    private ContactsCategory category;  //组
    @Column(column = "name")
    private String name;        //姓名
    @Column(column = "sex")
    private String sex;            //性别
    @Column(column = "nickName")
    private String nickName;    //绰号
    @Column(column = "birthday")
    private String birthday;    //生日
    @Column(column = "companyName")
    @SerializedName("deptName")
    private String companyName;    //公司名称
    @Column(column = "companyAddr")
    @SerializedName("deptAddr")
    private String companyAddr;    //公司地址
    @Column(column = "homeTel")
    private String homeTel;     //电话
    @Column(column = "mobile")
    private String mobile;      //手机
    @Column(column = "ministration")
    private String ministration;//职务

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ContactsCategory getCategory() {
        return category;
    }

    public void setCategory(ContactsCategory category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddr() {
        return companyAddr;
    }

    public void setCompanyAddr(String companyAddr) {
        this.companyAddr = companyAddr;
    }

    public String getHomeTel() {
        return homeTel;
    }

    public void setHomeTel(String homeTel) {
        this.homeTel = homeTel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMinistration() {
        return ministration;
    }

    public void setMinistration(String ministration) {
        this.ministration = ministration;
    }
}
