package com.jxd.oa.bean;

import com.google.gson.annotations.SerializedName;
import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Foreign;
import com.yftools.db.annotation.Table;

import java.util.Date;

/**
 * *****************************************
 * Description ：报销单
 * Created by cy on 2014/10/24.
 * *****************************************
 */
@Table(name = "t_expense_account")
public class ExpenseAccount extends AbstractBean {
    private String id;//主键
    @Foreign(column = "applyUserId", foreign = "id")
    @SerializedName("user")
    private User applyUser;//申请者
    private String itemName;//报销项目
    private int type;//类型 车费1，餐费2，住宿费3，其它4
    private Date applyDate;//报销日期
    private Double money;//报销金额
    private String remark;//备注
    @Foreign(column = "auditUserId", foreign = "id")
    private User auditUser;//审批人
    private int auditStatus;//审核状态 1待办 2同意 3拒绝
    private String auditRemark;//审核内容
    private Date modifiedDate;//创建、修改时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(User applyUser) {
        this.applyUser = applyUser;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public User getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(User auditUser) {
        this.auditUser = auditUser;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
