package com.alpha.customerservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import com.alpha.customerservice.requestdto.VehicleFare;

@Entity
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int customerId;

	private String pickupLocation;
	private String destinationLocation;

	private double sourceLatitude;
	private double sourceLongitude;

	private double destinationLatitude;
	private double destinationLongitude;

	private String paymentType;

	private VehicleFare vehicleType;

	private int riderId;

	private String bookingDate;
	private String bookingTime;
	private String pickupTime;
	private String dropTime;

	private double fare;

	public Booking() {
		super();
	}

	public Booking(int customerId, String pickupLocation, String destinationLocation, double sourceLatitude,
			double sourceLongitude, double destinationLatitude, double destinationLongitude, String paymentType,
			VehicleFare vehicleType, int riderId, String bookingDate, String bookingTime, String pickupTime,
			String dropTime, double fare) {

		super();

		this.customerId = customerId;
		this.pickupLocation = pickupLocation;
		this.destinationLocation = destinationLocation;
		this.sourceLatitude = sourceLatitude;
		this.sourceLongitude = sourceLongitude;
		this.destinationLatitude = destinationLatitude;
		this.destinationLongitude = destinationLongitude;
		this.paymentType = paymentType;
		this.vehicleType = vehicleType;
		this.riderId = riderId;
		this.bookingDate = bookingDate;
		this.bookingTime = bookingTime;
		this.pickupTime = pickupTime;
		this.dropTime = dropTime;
		this.fare = fare;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
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

	public double getSourceLatitude() {
		return sourceLatitude;
	}

	public void setSourceLatitude(double sourceLatitude) {
		this.sourceLatitude = sourceLatitude;
	}

	public double getSourceLongitude() {
		return sourceLongitude;
	}

	public void setSourceLongitude(double sourceLongitude) {
		this.sourceLongitude = sourceLongitude;
	}

	public double getDestinationLatitude() {
		return destinationLatitude;
	}

	public void setDestinationLatitude(double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}

	public double getDestinationLongitude() {
		return destinationLongitude;
	}

	public void setDestinationLongitude(double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public VehicleFare getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleFare vehicleType) {
		this.vehicleType = vehicleType;
	}

	public int getRiderId() {
		return riderId;
	}

	public void setRiderId(int riderId) {
		this.riderId = riderId;
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

	public double getFare() {
		return fare;
	}

	public void setFare(double fare) {
		this.fare = fare;
	}
}