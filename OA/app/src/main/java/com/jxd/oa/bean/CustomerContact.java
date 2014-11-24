package com.jxd.oa.bean;

import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.NoAutoIncrement;
import com.yftools.db.annotation.Table;
import com.yftools.db.annotation.Unique;

/**
 * *****************************************
 * Description ：
 * Created by cy on 2014/11/6.
 * *****************************************
 */
@Table(name = "t_customer_contact")
public class CustomerContact extends AbstractBean {
    private int id;
    private int customerId;
    @Unique
    private String contactName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
