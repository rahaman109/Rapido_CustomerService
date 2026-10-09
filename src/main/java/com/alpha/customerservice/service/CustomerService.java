package com.alpha.customerservice.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.customerservice.entity.Booking;
import com.alpha.customerservice.entity.Customer;
import com.alpha.customerservice.entity.LocationCoOrdinates;
import com.alpha.customerservice.exception.CustomerNotFoundException;
import com.alpha.customerservice.repository.BookingRepository;
import com.alpha.customerservice.repository.CustomerServiceRepository;
import com.alpha.customerservice.requestdto.ConfirmRideRequestDto;
import com.alpha.customerservice.requestdto.CustomerRequestDto;
import com.alpha.customerservice.requestdto.CustomerSelectRideRequestDto;
import com.alpha.customerservice.requestdto.FindNearByRiderRequestDto;
import com.alpha.customerservice.requestdto.VehicleFare;
import com.alpha.customerservice.responsedto.BookingRideResponseDto;
import com.alpha.customerservice.responsedto.CustomerResponseDto;
import com.alpha.customerservice.responsedto.ResponseStructure;
import com.alpha.customerservice.responsedto.RiderFareResponse;
import com.alpha.customerservice.responsedto.SearchDestionationLocationResponseDto;

@Service
public class CustomerService {

	@Autowired
	private CustomerServiceRepository customerServiceRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private BookingRepository bookingRepository;

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

	public ResponseStructure<RiderFareResponse> selectRide(CustomerSelectRideRequestDto customerSelectRideDto) {

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

		RiderFareResponse riderFareResponse = new RiderFareResponse();

		riderFareResponse.setAutoFare(autofare);
		riderFareResponse.setBikeFare(bikefare);
		riderFareResponse.setCarFare(cabfare);

		riderFareResponse.setPickupAddress(customerSelectRideDto.getSourceAddress());
		riderFareResponse.setPickupLocation(customerSelectRideDto.getSourceLocation());
		riderFareResponse.setDestinationAddress(customerSelectRideDto.getDestinationAddress());
		riderFareResponse.setDestinationLocation(customerSelectRideDto.getDestinationLocation());

		// Storing data inside Redis
		String key = "rideDetails:" + customerSelectRideDto.getCustomerId();

		// And then I return this response to client side
		redisTemplate.opsForValue().set(key, riderFareResponse);

		return new ResponseStructure<>(HttpStatus.OK.value(), "Selecting Ride SuccessFully Done", riderFareResponse);
	}

	public ResponseStructure<BookingRideResponseDto> confirmRide(ConfirmRideRequestDto confirmRideRequestDto) {

		Customer customer = customerServiceRepository.findById(confirmRideRequestDto.getCustomerId())
				.orElseThrow(() -> new CustomerNotFoundException());

		String key = "rideDetails:" + confirmRideRequestDto.getCustomerId();

		RiderFareResponse riderFareResponse = (RiderFareResponse) redisTemplate.opsForValue().get(key);

		if (riderFareResponse == null) {
			throw new RuntimeException("Ride details not found");
		}

		// Calculate fare
		double fare = 0;

		if (confirmRideRequestDto.getVehicleType() == VehicleFare.BIKE) {

			fare = riderFareResponse.getBikeFare();

		} else if (confirmRideRequestDto.getVehicleType() == VehicleFare.AUTO) {

			fare = riderFareResponse.getAutoFare();

		} else if (confirmRideRequestDto.getVehicleType() == VehicleFare.CAR) {

			fare = riderFareResponse.getCarFare();

		} else {

			throw new RuntimeException("Invalid Vehicle Type");
		}
		
		Booking booking = new Booking();

		booking.setCustomerId(customer.getId());

		booking.setPickupLocation(riderFareResponse.getPickupAddress());

		booking.setDestinationLocation(riderFareResponse.getDestinationAddress());

		booking.setSourceLatitude(riderFareResponse.getPickupLocation().getLatitude());

		booking.setSourceLongitude(riderFareResponse.getPickupLocation().getLongitude());

		booking.setDestinationLatitude(riderFareResponse.getDestinationLocation().getLatitude());

		booking.setDestinationLongitude(riderFareResponse.getDestinationLocation().getLongitude());

		booking.setPaymentType("PENDING");

		booking.setVehicleType(confirmRideRequestDto.getVehicleType());

		booking.setBookingDate(LocalDate.now().toString());

		booking.setBookingTime(LocalTime.now().toString());

		booking.setFare(fare);

		// Find nearby riders
		FindNearByRiderRequestDto findNearByRiderRequestDto = new FindNearByRiderRequestDto();

		findNearByRiderRequestDto.setVehicleType(confirmRideRequestDto.getVehicleType().toString());

		LocationCoOrdinates locationCoOrdinates = new LocationCoOrdinates(
				riderFareResponse.getPickupLocation().getLatitude(),
				riderFareResponse.getPickupLocation().getLongitude());

		findNearByRiderRequestDto.setLocationCoordinates(locationCoOrdinates);

		ResponseEntity<ResponseStructure> response = restTemplate.postForEntity(
				"http://localhost:8083/rider/findNearByRiders", findNearByRiderRequestDto, ResponseStructure.class);

		List<Integer> riders = (List<Integer>) response.getBody().getData();

		if (riders == null || riders.isEmpty()) {
			throw new RuntimeException("No nearby riders available");
		}

		// Assign first nearby rider
		int riderId = riders.get(0);

		booking.setRiderId(riderId);

		// Save booking in db
		Booking saveBooking = bookingRepository.save(booking);

		// Store complete booking data in Redis
		String rideKey = "ride:" + saveBooking.getId();

		redisTemplate.opsForHash().put(rideKey, "bookingId", String.valueOf(saveBooking.getId()));

		redisTemplate.opsForHash().put(rideKey, "customerId", String.valueOf(saveBooking.getCustomerId()));

		redisTemplate.opsForHash().put(rideKey, "pickupLocation", saveBooking.getPickupLocation());

		redisTemplate.opsForHash().put(rideKey, "destinationLocation", saveBooking.getDestinationLocation());

		redisTemplate.opsForHash().put(rideKey, "sourceLatitude", String.valueOf(saveBooking.getSourceLatitude()));

		redisTemplate.opsForHash().put(rideKey, "sourceLongitude", String.valueOf(saveBooking.getSourceLongitude()));

		redisTemplate.opsForHash().put(rideKey, "destinationLatitude",
				String.valueOf(saveBooking.getDestinationLatitude()));

		redisTemplate.opsForHash().put(rideKey, "destinationLongitude",
				String.valueOf(saveBooking.getDestinationLongitude()));

		redisTemplate.opsForHash().put(rideKey, "paymentType", saveBooking.getPaymentType());

		redisTemplate.opsForHash().put(rideKey, "vehicleType", saveBooking.getVehicleType().toString());

		redisTemplate.opsForHash().put(rideKey, "riderId", String.valueOf(saveBooking.getRiderId()));

		redisTemplate.opsForHash().put(rideKey, "bookingDate", saveBooking.getBookingDate());

		redisTemplate.opsForHash().put(rideKey, "bookingTime", saveBooking.getBookingTime());

		redisTemplate.opsForHash().put(rideKey, "pickupTime", String.valueOf(saveBooking.getPickupTime()));

		redisTemplate.opsForHash().put(rideKey, "dropTime", String.valueOf(saveBooking.getDropTime()));

		redisTemplate.opsForHash().put(rideKey, "fare", String.valueOf(saveBooking.getFare()));

		// Store booking reference under rider
		String riderKey = "rider:" + riderId + ":requests";

		redisTemplate.opsForHash().put(riderKey, String.valueOf(saveBooking.getId()), rideKey);

		BookingRideResponseDto responseDto = new BookingRideResponseDto();

		responseDto.setCustomerId(saveBooking.getCustomerId());

		responseDto.setRiderId(saveBooking.getRiderId());

		responseDto.setSourceLatitude(saveBooking.getSourceLatitude());

		responseDto.setSourceLongitude(saveBooking.getSourceLongitude());

		responseDto.setDestinationLatitude(saveBooking.getDestinationLatitude());

		responseDto.setDestinationLongitude(saveBooking.getDestinationLongitude());

		responseDto.setFare(saveBooking.getFare());

		return new ResponseStructure<>(HttpStatus.OK.value(), "Confirm Ride", responseDto);
	}

	public ResponseStructure<Boolean> otpValidation(int customerId, String otp) {
		Customer customer = customerServiceRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException());
		if(!customer.getOtp().equals(otp))
		{
			throw new RuntimeException("Otp Invalid");
		}
		return new ResponseStructure<>(
				HttpStatus.OK.value(),
				"ACCEPTED",
				true);
	}
}
