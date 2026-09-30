package com.alpha.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.customerservice.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Integer>{

}
