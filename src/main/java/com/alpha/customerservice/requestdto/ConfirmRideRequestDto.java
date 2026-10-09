package com.alpha.customerservice.requestdto;

public class ConfirmRideRequestDto {

	private int customerId;
	private VehicleFare vehicleType;

	public ConfirmRideRequestDto(int customerId, VehicleFare vehicleType) {
		super();
		this.customerId = customerId;
		this.vehicleType = vehicleType;
	}

	public ConfirmRideRequestDto() {
		super();
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public VehicleFare getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleFare vehicleType) {
		this.vehicleType = vehicleType;
	}
}
