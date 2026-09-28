package com.alpha.customerservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.customerservice.entity.Customer;
import com.alpha.customerservice.exception.CustomerNotFoundException;
import com.alpha.customerservice.repository.CustomerServiceRepository;
import com.alpha.customerservice.requestdto.CustomerRequestDto;
import com.alpha.customerservice.requestdto.CustomerSelectRideDto;
import com.alpha.customerservice.requestdto.VehicleFare;
import com.alpha.customerservice.responsedto.CustomerResponseDto;
import com.alpha.customerservice.responsedto.ResponseStructure;
import com.alpha.customerservice.responsedto.SearchDestionationLocationResponseDto;

@Service
public class CustomerService {

	@Autowired
	private CustomerServiceRepository customerServiceRepository;

	@Autowired
	private RestTemplate restTemplate;

	public double getFare(VehicleFare vehicleFare) {
		switch (vehicleFare) {
		case BIKE:
			return 10;
		case AUTO:
			return 20;
		case CAR:
			return 30;
		default:
			throw new IllegalArgumentException("Invalid vehicle type");
		}
	}

	public ResponseStructure<CustomerResponseDto> saveCustomer(CustomerRequestDto customerRequestDto) {

		Customer customer = new Customer();

		customer.setName(customerRequestDto.getName());
		customer.setEmail(customerRequestDto.getMail());
		customer.setMobile(customerRequestDto.getMobile());
		customer.setGender(customerRequestDto.getGender());

		Customer saveCustomerWithOutId = customerServiceRepository.save(customer);
		String otp = String.format("%04d", saveCustomerWithOutId.getId());
		customer.setOtp(otp);
		Customer saveCustomerWithId = customerServiceRepository.save(customer);

		CustomerResponseDto customerResponseDto = new CustomerResponseDto();

		customerResponseDto.setCustomerId(saveCustomerWithId.getId());
		customerResponseDto.setName(saveCustomerWithId.getName());
		customerResponseDto.setMail(saveCustomerWithId.getEmail());
		customerResponseDto.setMobile(saveCustomerWithId.getMobile());

		return new ResponseStructure<>(HttpStatus.CREATED.value(), "Customer save sucessfully", customerResponseDto);
	}

	public ResponseStructure<CustomerResponseDto> deleteCustomer(int customerId) {

		Customer customer = customerServiceRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException());

		customerServiceRepository.deleteById(customerId);

		CustomerResponseDto customerResponseDto = new CustomerResponseDto();
		customerResponseDto.setCustomerId(customer.getId());
		customerResponseDto.setName(customer.getName());
		customerResponseDto.setMail(customer.getEmail());
		customerResponseDto.setMobile(customer.getMobile());

		return new ResponseStructure<>(HttpStatus.OK.value(), "Customer Deleted Successfully", customerResponseDto);
	}

	public ResponseStructure<CustomerResponseDto> customerFindById(int customerId) {

		Customer customer = customerServiceRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException());

		CustomerResponseDto customerResponseDto = new CustomerResponseDto();

		customerResponseDto.setCustomerId(customer.getId());
		customerResponseDto.setName(customer.getName());
		customerResponseDto.setMobile(customer.getMobile());
		customerResponseDto.setMail(customer.getEmail());

		return new ResponseStructure<>(HttpStatus.OK.value(), "Customer Found Successfully", customerResponseDto);
	}

	public ResponseStructure<List<SearchDestionationLocationResponseDto>> customerGetLocation(String location) {

		String url = "https://us1.locationiq.com/v1/search?key=pk.a4b4121444098fe2fdcd6c9171b061df&q=" + location
				+ "&format=json&";
		ArrayList<Object> list = restTemplate.getForObject(url, ArrayList.class);
		List<SearchDestionationLocationResponseDto> searchDestionationLocationResponseDtolist = new ArrayList<SearchDestionationLocationResponseDto>();
		for (Object obj : list) {
			Map<String, Object> map = (Map<String, Object>) obj;
			SearchDestionationLocationResponseDto responseDto = new SearchDestionationLocationResponseDto();
			responseDto.setAddress((String) map.get("display_name"));
			responseDto.setLatitude(Double.parseDouble((String) map.get("lat")));
			responseDto.setLongitude(Double.parseDouble((String) map.get("lon")));
			searchDestionationLocationResponseDtolist.add(responseDto);
		}
		return new ResponseStructure<List<SearchDestionationLocationResponseDto>>(HttpStatus.OK.value(), "Done",
				searchDestionationLocationResponseDtolist);
	}

	public void selectRide(CustomerSelectRideDto customerSelectRideDto) {

		double sourceLatitude = customerSelectRideDto.getSourceLocation().getLatitude();
		double sourceLongitude = customerSelectRideDto.getSourceLocation().getLongitude();

		double destinationLatitude = customerSelectRideDto.getDestinationLocation().getLatitude();
		double destinationLongitude = customerSelectRideDto.getDestinationLocation().getLongitude();

		String url = "https://us1.locationiq.com/v1/directions/driving/" + sourceLongitude + "," + sourceLatitude + ";"
				+ destinationLongitude + "," + destinationLatitude + "?key=pk.a4b4121444098fe2fdcd6c9171b061df"
				+ "&steps=true" + "&alternatives=true" + "&geometries=polyline" + "&overview=full";

		Map<String, Object> mapResponse = restTemplate.getForObject(url, Map.class);
		List<Map<String, Object>> routes = (List<Map<String, Object>>) mapResponse.get("routes");
		Map<String, Object> route = routes.get(0);
		double distanceInMeters = ((Number) route.get("distance")).doubleValue();
		double distanceInKm = distanceInMeters / 1000;
		System.out.println("Distance: " + distanceInKm + "km");

		double bikefare = distanceInKm * getFare(VehicleFare.BIKE);
		double autofare = distanceInKm * getFare(VehicleFare.AUTO);
		double cabfare = distanceInKm * getFare(VehicleFare.CAR);
		
		System.out.println( "For bike " +bikefare + " Auto  " + autofare + " car" + cabfare);

	}
}
