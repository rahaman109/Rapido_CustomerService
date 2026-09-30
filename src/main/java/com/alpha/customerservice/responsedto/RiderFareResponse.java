package com.alpha.customerservice.responsedto;

import com.alpha.customerservice.entity.LocationCoOrdinates;

public class RiderFareResponse {

	private String pickupAddress;
	private String destinationAddress;
	private LocationCoOrdinates pickupLocation;
	private LocationCoOrdinates destinationLocation;

	private double bikeFare;
	private double autoFare;
	private double carFare;

	public RiderFareResponse(String pickupAddress, String destinationAddress, LocationCoOrdinates pickupLocation,
			LocationCoOrdinates destinationLocation, double bikeFare, double autoFare, double carFare) {
		super();
		this.pickupAddress = pickupAddress;
		this.destinationAddress = destinationAddress;
		this.pickupLocation = pickupLocation;
		this.destinationLocation = destinationLocation;
		this.bikeFare = bikeFare;
		this.autoFare = autoFare;
		this.carFare = carFare;
	}

	public RiderFareResponse() {
		super();
	}

	public String getPickupAddress() {
		return pickupAddress;
	}

	public void setPickupAddress(String pickupAddress) {
		this.pickupAddress = pickupAddress;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public LocationCoOrdinates getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(LocationCoOrdinates pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public LocationCoOrdinates getDestinationLocation() {
		return destinationLocation;
	}

	public void setDestinationLocation(LocationCoOrdinates destinationLocation) {
		this.destinationLocation = destinationLocation;
	}

	public double getBikeFare() {
		return bikeFare;
	}

	public void setBikeFare(double bikeFare) {
		this.bikeFare = bikeFare;
	}

	public double getAutoFare() {
		return autoFare;
	}

	public void setAutoFare(double autoFare) {
		this.autoFare = autoFare;
	}

	public double getCarFare() {
		return carFare;
	}

	public void setCarFare(double carFare) {
		this.carFare = carFare;
	}

}
