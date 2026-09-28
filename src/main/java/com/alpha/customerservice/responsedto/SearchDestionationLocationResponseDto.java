package com.alpha.customerservice.responsedto;

public class SearchDestionationLocationResponseDto {

	private String address;
	private double latitude;
	private double longitude;

	public SearchDestionationLocationResponseDto(String address, double latitude, double longitude) {
		super();
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public SearchDestionationLocationResponseDto() {
		super();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

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

}
