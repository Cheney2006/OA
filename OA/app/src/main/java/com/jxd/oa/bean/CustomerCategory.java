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
@Table(name = "t_customer_category")
public class CustomerCategory extends AbstractBean {
    private int id;
    private String name;

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
}
