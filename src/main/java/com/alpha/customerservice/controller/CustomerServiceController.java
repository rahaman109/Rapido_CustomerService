package com.alpha.customerservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.customerservice.requestdto.ConfirmRideRequestDto;
import com.alpha.customerservice.requestdto.CustomerRequestDto;
import com.alpha.customerservice.requestdto.CustomerSelectRideRequestDto;
import com.alpha.customerservice.responsedto.BookingRideResponseDto;
import com.alpha.customerservice.responsedto.CustomerResponseDto;
import com.alpha.customerservice.responsedto.ResponseStructure;
import com.alpha.customerservice.responsedto.RiderFareResponse;
import com.alpha.customerservice.responsedto.SearchDestionationLocationResponseDto;
import com.alpha.customerservice.service.CustomerService;

@RestController
public class CustomerServiceController {

	@Autowired
	private CustomerService customerService;
	
	@PostMapping("/customer/save")
	public ResponseStructure<CustomerResponseDto> saveCustomer(@RequestBody CustomerRequestDto customerRequestDto)
	{
		return customerService.saveCustomer(customerRequestDto);
	}
	
	@DeleteMapping("/customer/delete")
	public ResponseStructure<CustomerResponseDto> deleteCustomer(@RequestParam int customerId)
	{
		return customerService.deleteCustomer(customerId);
	}
	
	@GetMapping("/customer/findById/{customerId}")
	public ResponseStructure<CustomerResponseDto> customerFindById(@PathVariable int customerId)
	{
		return customerService.customerFindById(customerId);
	}
	
	@GetMapping("/customer/location")
	public ResponseStructure<List<SearchDestionationLocationResponseDto>> customerGetLocation(@RequestParam String location)
	{
		return customerService.customerGetLocation(location);
	}
	
	
	@PostMapping("/customer/selectride")
	public ResponseStructure<RiderFareResponse> selectRide(@RequestBody CustomerSelectRideRequestDto customerSelectRideDto)
	{
		return customerService.selectRide(customerSelectRideDto);
	}
	
	@PostMapping("/customer/confirmride")
	public ResponseStructure<BookingRideResponseDto> confirmRide(@RequestBody ConfirmRideRequestDto confirmRideRequestDto)
	{
		return customerService.confirmRide(confirmRideRequestDto);
	}
	
	@PostMapping("/customer/otp")
	public ResponseStructure<Boolean> otpValidation(@RequestParam int customerId, @RequestParam String otp)
	{
		return customerService.otpValidation(customerId, otp);
	}
}
