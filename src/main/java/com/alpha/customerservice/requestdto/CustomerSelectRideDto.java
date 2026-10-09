package com.alpha.customerservice.requestdto;

import com.alpha.customerservice.entity.Location;

public class CustomerSelectRideDto {
	
	private int customerId;
	private Location sourceLocation;
	private Location destinationLocation;
	public CustomerSelectRideDto(int customerId, Location sourceLocation,
			Location destinationLocation) {
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