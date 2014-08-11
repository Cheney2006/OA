package com.jxd.common.vo;


public class LocationInfo {
	private double latitude;
	private double longitude;
    private String address;

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

    public static LocationInfo getDefaultLocation(){
		LocationInfo locationInfo=new LocationInfo();
		locationInfo.latitude=30.599736;
		locationInfo.longitude=114.309876;
		return locationInfo;
	}
}
