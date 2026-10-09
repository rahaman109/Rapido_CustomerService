package com.alpha.customerservice.responsedto;

import com.alpha.customerservice.entity.Location;

public class RiderFareResponse {

	private String pickupAddress;
	private String destinationAddress;
	private Location pickupLocation;
	private Location destinationLocation;

	private double bikeFare;
	private double autoFare;
	private double carFare;

	public RiderFareResponse(String pickupAddress, String destinationAddress, Location pickupLocation,
			Location destinationLocation, double bikeFare, double autoFare, double carFare) {
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

	public Location getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(Location pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public Location getDestinationLocation() {
		return destinationLocation;
	}

	public void setDestinationLocation(Location destinationLocation) {
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