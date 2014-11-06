package com.jxd.oa.bean;

import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Foreign;
import com.yftools.db.annotation.ForeignCollection;
import com.yftools.db.annotation.NoAutoIncrement;
import com.yftools.db.annotation.Table;
import com.yftools.db.sqlite.ForeignCollectionLazyLoader;

/**
 * *****************************************
 * Description ：测试自增主键
 * Created by cy on 2014/11/6.
 * *****************************************
 */
@Table(name = "t_customer")
public class Customer extends AbstractBean {
    @NoAutoIncrement
    private int id;
    private String name;
    @ForeignCollection(valueColumn = "id", foreign = "customerId", foreignAutoCreate = true)
    private ForeignCollectionLazyLoader<CustomerContact> CustomerContactList;//该对像不能序列化,transient
    @Foreign(column = "categoryId", foreign = "id", foreignAutoCreate = true)
    private CustomerCategory customerCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ForeignCollectionLazyLoader<CustomerContact> getCustomerContactList() {
        return CustomerContactList;
    }

    public void setCustomerContactList(ForeignCollectionLazyLoader<CustomerContact> customerContactList) {
        CustomerContactList = customerContactList;
    }

    public CustomerCategory getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(CustomerCategory customerCategory) {
        this.customerCategory = customerCategory;
    }
}
