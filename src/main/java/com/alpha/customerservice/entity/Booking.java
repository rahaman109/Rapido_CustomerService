package com.alpha.customerservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int customerId;
    @OneToOne
    private Address pickupLocationId;
    @OneToOne
    private Address destinationLocationId;

    private String paymentType;
    private String vehicleType;
    private int riderId;

    private String bookingDate;
    private String bookingTime;
    private String pickupTime;
    private String dropTime;

    private int fare;

    public Booking() {
        super();
    }

    public Booking(int customerId, Address pickupLocationId, Address destinationLocationId,
            String paymentType, String vehicleType, int riderId,
            String bookingDate, String bookingTime, String pickupTime,
            String dropTime, int fare) {

        this.customerId = customerId;
        this.pickupLocationId = pickupLocationId;
        this.destinationLocationId = destinationLocationId;
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

    public Address getPickupLocationId() {
        return pickupLocationId;
    }

    public void setPickupLocationId(Address pickupLocationId) {
        this.pickupLocationId = pickupLocationId;
    }

    public Address getDestinationLocationId() {
        return destinationLocationId;
    }

    public void setDestinationLocationId(Address destinationLocationId) {
        this.destinationLocationId = destinationLocationId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
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

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }
}