package com.jxd.oa.bean;

import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Column;
import com.yftools.db.annotation.Foreign;
import com.yftools.db.annotation.Id;
import com.yftools.db.annotation.Table;

import java.util.Date;

/**
 * *****************************************
 * Description ：考勤
 * Created by cywf on 2014/8/9.
 * *****************************************
 */
@Table(name = "t_sign")
public class Sign extends AbstractBean {
    @Id(column = "id")
    private String id;
    @Column(column = "signTime")
    private Date signTime;//考勤时间
    @Column(column = "signAddress")
    private String signAddress;//考勤地址
    @Column(column = "signLatitude")
    private Double signLatitude;//纬度
    @Column(column = "signLongitude")
    private Double signLongitude;//经度
    @Column(column = "signType")
    private Integer signType;//类型(1:签到,2:签退)
    @Column(column = "signCoorType")
    private Integer signCoorType; //坐标类型 1 国测局 2 WGS84
    @Column(column = "signAccuracy")
    private Float signAccuracy;//定位精度
    @Column(column = "signDistance")
    private Double signDistance;//偏差距离(米)
    @Foreign(column = "vicinityAddressId", foreign = "id")
    private Address vicinityAddress;//参考位置

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getSignAddress() {
        return signAddress;
    }

    public void setSignAddress(String signAddress) {
        this.signAddress = signAddress;
    }

    public Double getSignLatitude() {
        return signLatitude;
    }

    public void setSignLatitude(Double signLatitude) {
        this.signLatitude = signLatitude;
    }

    public Double getSignLongitude() {
        return signLongitude;
    }

    public void setSignLongitude(Double signLongitude) {
        this.signLongitude = signLongitude;
    }

    public Integer getSignType() {
        return signType;
    }

    public void setSignType(Integer signType) {
        this.signType = signType;
    }

    public Integer getSignCoorType() {
        return signCoorType;
    }

    public void setSignCoorType(Integer signCoorType) {
        this.signCoorType = signCoorType;
    }

    public Float getSignAccuracy() {
        return signAccuracy;
    }

    public void setSignAccuracy(Float signAccuracy) {
        this.signAccuracy = signAccuracy;
    }

    public Double getSignDistance() {
        return signDistance;
    }

    public void setSignDistance(Double signDistance) {
        this.signDistance = signDistance;
    }

    public Address getVicinityAddress() {
        return vicinityAddress;
    }

    public void setVicinityAddress(Address vicinityAddress) {
        this.vicinityAddress = vicinityAddress;
    }
}
