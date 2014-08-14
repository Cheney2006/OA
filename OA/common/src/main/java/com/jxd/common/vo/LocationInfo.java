package com.jxd.common.vo;


public class LocationInfo {

	private double latitude;
	private double longitude;
    private String address;
    private float accuracy;
    private String coorType;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public String getCoorType() {
        return coorType;
    }

    public void setCoorType(String coorType) {
        this.coorType = coorType;
    }

    public static LocationInfo getDefaultLocation(){
		LocationInfo locationInfo=new LocationInfo();
		locationInfo.latitude=30.599736;
		locationInfo.longitude=114.309876;
		return locationInfo;
	}
}
