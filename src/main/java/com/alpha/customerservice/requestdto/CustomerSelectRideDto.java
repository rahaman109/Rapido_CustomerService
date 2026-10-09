package com.alpha.customerservice.requestdto;

import com.alpha.customerservice.entity.Location;

public class CustomerSelectRideDto {

	private int customerId;
	private String sourceAddress;
	private String destinationAddress;
	private Location sourceLocation;
	private Location destinationLocation;

	public CustomerSelectRideDto() {
		super();
	}

	public CustomerSelectRideDto(int customerId, String sourceAddress, String destinationAddress,
			Location sourceLocation, Location destinationLocation) {
		this.customerId = customerId;
		this.sourceAddress = sourceAddress;
		this.destinationAddress = destinationAddress;
		this.sourceLocation = sourceLocation;
		this.destinationLocation = destinationLocation;
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

	public Location getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(Location sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public Location getDestinationLocation() {
		return destinationLocation;
	}

	public void setDestinationLocation(Location destinationLocation) {
		this.destinationLocation = destinationLocation;
	}
}