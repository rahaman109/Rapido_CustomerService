package com.alpha.customerservice.responsedto;

public class CustomerRideHistoryRepositoryResponseDto {

	private int bookingId;
	private int customerId;
	private int riderId;

	private String pickupLocation;
	private String destinationLocation;

	private String vehicleType;
	private String paymentType;
	private String bookingDate;
	private String bookingTime;
	private String pickupTime;
	private String dropTime;
	private String status;

	private double fare;

	public CustomerRideHistoryRepositoryResponseDto(int bookingId, int customerId, int riderId, String pickupLocation,
			String destinationLocation, String vehicleType, String paymentType, String bookingDate, String bookingTime,
			String pickupTime, String dropTime, String status, double fare) {
		super();
		this.bookingId = bookingId;
		this.customerId = customerId;
		this.riderId = riderId;
		this.pickupLocation = pickupLocation;
		this.destinationLocation = destinationLocation;
		this.vehicleType = vehicleType;
		this.paymentType = paymentType;
		this.bookingDate = bookingDate;
		this.bookingTime = bookingTime;
		this.pickupTime = pickupTime;
		this.dropTime = dropTime;
		this.status = status;
		this.fare = fare;
	}

	public CustomerRideHistoryRepositoryResponseDto() {
		super();
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getRiderId() {
		return riderId;
	}

	public void setRiderId(int riderId) {
		this.riderId = riderId;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getDestinationLocation() {
		return destinationLocation;
	}

	public void setDestinationLocation(String destinationLocation) {
		this.destinationLocation = destinationLocation;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getDropTime() {
		return dropTime;
	}

	public void setDropTime(String dropTime) {
		this.dropTime = dropTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getFare() {
		return fare;
	}

	public void setFare(double fare) {
		this.fare = fare;
	}

}
