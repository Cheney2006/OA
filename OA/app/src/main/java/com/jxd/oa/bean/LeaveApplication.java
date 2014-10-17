package com.jxd.oa.bean;

import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Foreign;
import com.yftools.db.annotation.Table;

import java.util.Date;

/**
 * *****************************************
 * Description ：请假单
 * Created by cy on 2014/10/16.
 * *****************************************
 */
@Table(name = "t_leave_application")
public class LeaveApplication extends AbstractBean {
    private String id;//主键
    @Foreign(column = "userId", foreign = "id")
    private User user;//申请者
    private String leaveReason;//请假事由
    private int type;//类型 事假1，病假2，年假3，其它4
    private String startDate;//开始时间 eg:2014-10-16 13:30
    private String endDate;//结束时间 eg:2014-10-16 17:30
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
}
