package com.alpha.customerservice.requestdto;

import com.alpha.customerservice.entity.locationCoOrdinates;

public class CustomerSelectRideDto {

	private int customerId;
	private locationCoOrdinates sourceLocation;
	private locationCoOrdinates destinationLocation;

	public CustomerSelectRideDto(int customerId, locationCoOrdinates sourceLocation,
			locationCoOrdinates destinationLocation) {
		super();
		this.customerId = customerId;
		this.sourceLocation = sourceLocation;
		this.destinationLocation = destinationLocation;
	}

	public CustomerSelectRideDto() {
		super();
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public locationCoOrdinates getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(locationCoOrdinates sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public locationCoOrdinates getDestinationLocation() {
		return destinationLocation;
	}

	public void setDestinationLocation(locationCoOrdinates destinationLocation) {
		this.destinationLocation = destinationLocation;
	}

}
