package com.alpha.customerservice.requestdto;

import com.alpha.customerservice.entity.Location;

public class FindNearByRiderRequestDto {

	private String vehicleType;
	private Location locationCoordinates;

	public FindNearByRiderRequestDto(String vehicleType, Location locationCoordinates) {
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

	public Location getLocationCoordinates() {
		return locationCoordinates;
	}

	public void setLocationCoordinates(Location locationCoordinates) {
		this.locationCoordinates = locationCoordinates;
	}

}
