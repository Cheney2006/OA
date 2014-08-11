package com.jxd.oa.bean;

import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;

/**
 * *****************************************
 * Description ：位置采集
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
@Table(name = "t_address")
public class Address extends AbstractBean {
    @Id(column = "id")
    private String id;
    @Column(column = "name")
    private String name;//采集名称
    @Column(column = "accuracy")
    private Float accuracy;//精度
    @Column(column = "address")
    private String address;//地址
    @Column(column = "coorType")
    private Integer coorType;//坐标类型
    @Column(column = "latitude")
    private Double latitude;//纬度
    @Column(column = "longitude")
    private Double longitude;//经度
    /**
     * 状态
     * 1 待采集——后台创建时状态
     * 2 提交采集——手机采集提交状态
     * 3 完成采集——手机提交，后台审核通过后状态
     * 4 废弃采集——后台取消采集状态或者后台重新采集状态
     */
    @Column(column = "status")
    private Integer status;
    @Column(column = "collectUserId")
    private String collectUserId;//采集人

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCoorType() {
        return coorType;
    }

    public void setCoorType(Integer coorType) {
        this.coorType = coorType;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCollectUserId() {
        return collectUserId;
    }

    public void setCollectUserId(String collectUserId) {
        this.collectUserId = collectUserId;
    }
}
