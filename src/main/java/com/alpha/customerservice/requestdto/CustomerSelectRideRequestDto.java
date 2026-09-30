package com.alpha.customerservice.requestdto;

import com.alpha.customerservice.entity.LocationCoOrdinates;

public class CustomerSelectRideRequestDto {

	private int customerId;
	private String sourceAddress;
	private String destinationAddress;
	private LocationCoOrdinates sourceLocation;
	private LocationCoOrdinates destinationLocation;

	public CustomerSelectRideRequestDto(int customerId, String sourceAddress, String destinationAddress,
			LocationCoOrdinates sourceLocation, LocationCoOrdinates destinationLocation) {
		super();
		this.customerId = customerId;
		this.sourceAddress = sourceAddress;
		this.destinationAddress = destinationAddress;
		this.sourceLocation = sourceLocation;
		this.destinationLocation = destinationLocation;
	}

	public CustomerSelectRideRequestDto() {
		super();
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public LocationCoOrdinates getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(LocationCoOrdinates sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public LocationCoOrdinates getDestinationLocation() {
		return destinationLocation;
	}

	public void setDestinationLocation(LocationCoOrdinates destinationLocation) {
		this.destinationLocation = destinationLocation;
	}

}
