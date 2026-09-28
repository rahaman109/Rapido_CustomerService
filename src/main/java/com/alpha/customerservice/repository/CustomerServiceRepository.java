package com.alpha.customerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alpha.customerservice.entity.Customer;

@Repository
public interface CustomerServiceRepository extends JpaRepository<Customer, Integer> {

}
