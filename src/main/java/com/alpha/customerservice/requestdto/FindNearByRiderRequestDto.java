package com.alpha.customerservice.requestdto;

import com.alpha.customerservice.entity.LocationCoOrdinates;

public class FindNearByRiderRequestDto {

	private String vehicleType;
	private LocationCoOrdinates locationCoordinates;

	public FindNearByRiderRequestDto(String vehicleType, LocationCoOrdinates locationCoordinates) {
		super();
		this.vehicleType = vehicleType;
		this.locationCoordinates = locationCoordinates;
	}

	public FindNearByRiderRequestDto() {
		super();
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public LocationCoOrdinates getLocationCoordinates() {
		return locationCoordinates;
	}

	public void setLocationCoordinates(LocationCoOrdinates locationCoordinates) {
		this.locationCoordinates = locationCoordinates;
	}

}
