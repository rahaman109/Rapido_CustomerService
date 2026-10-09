package com.alpha.customerservice.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.ObjectMapper;

import com.alpha.customerservice.entity.Booking;
import com.alpha.customerservice.entity.Customer;
import com.alpha.customerservice.entity.Location;
import com.alpha.customerservice.exception.CustomerNotFoundException;
import com.alpha.customerservice.repository.BookingRepository;
import com.alpha.customerservice.repository.CustomerServiceRepository;
import com.alpha.customerservice.requestdto.ConfirmRideRequestDto;
import com.alpha.customerservice.requestdto.CustomerRequestDto;
import com.alpha.customerservice.requestdto.CustomerSelectRideDto;
import com.alpha.customerservice.requestdto.FindNearByRiderRequestDto;
import com.alpha.customerservice.requestdto.VehicleFare;
import com.alpha.customerservice.responsedto.BookingRideResponseDto;
import com.alpha.customerservice.responsedto.CustomerResponseDto;
import com.alpha.customerservice.responsedto.CustomerRideHistoryRepositoryResponseDto;
import com.alpha.customerservice.responsedto.ResponseStructure;
import com.alpha.customerservice.responsedto.RiderFareResponse;
import com.alpha.customerservice.responsedto.SearchDestionationLocationResponseDto;

@Service
public class CustomerService {

	@Autowired
	private CustomerServiceRepository customerServiceRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${locationiq.api.key}")
	private String locationIqApiKey;

	// Calculate fare per kilometre
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

	// Save customer
	public ResponseStructure<CustomerResponseDto> saveCustomer(CustomerRequestDto customerRequestDto) {

		Customer customer = new Customer();

		customer.setName(customerRequestDto.getName());
		customer.setEmail(customerRequestDto.getMail());
		customer.setMobile(customerRequestDto.getMobile());
		customer.setGender(customerRequestDto.getGender());

		Customer savedCustomer = customerServiceRepository.save(customer);

		String otp = String.format("%04d", savedCustomer.getId());

		savedCustomer.setOtp(otp);

		savedCustomer = customerServiceRepository.save(savedCustomer);

		CustomerResponseDto responseDto = new CustomerResponseDto();

		responseDto.setCustomerId(savedCustomer.getId());
		responseDto.setName(savedCustomer.getName());
		responseDto.setMail(savedCustomer.getEmail());
		responseDto.setMobile(savedCustomer.getMobile());

		return new ResponseStructure<>(HttpStatus.CREATED.value(), "Customer saved successfully", responseDto);
	}

	// Delete customer
	public ResponseStructure<CustomerResponseDto> deleteCustomer(int customerId) {

		Customer customer = customerServiceRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException());

		customerServiceRepository.deleteById(customerId);

		CustomerResponseDto responseDto = new CustomerResponseDto();

		responseDto.setCustomerId(customer.getId());
		responseDto.setName(customer.getName());
		responseDto.setMail(customer.getEmail());
		responseDto.setMobile(customer.getMobile());

		return new ResponseStructure<>(HttpStatus.OK.value(), "Customer deleted successfully", responseDto);
	}

	// Find customer by ID
	public ResponseStructure<CustomerResponseDto> customerFindById(int customerId) {

		Customer customer = customerServiceRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException());

		CustomerResponseDto responseDto = new CustomerResponseDto();

		responseDto.setCustomerId(customer.getId());
		responseDto.setName(customer.getName());
		responseDto.setMail(customer.getEmail());
		responseDto.setMobile(customer.getMobile());

		return new ResponseStructure<>(HttpStatus.OK.value(), "Customer found successfully", responseDto);
	}

	// Search location
	@SuppressWarnings("unchecked")
	public ResponseStructure<List<SearchDestionationLocationResponseDto>> customerGetLocation(String location) {

		String encodedLocation = URLEncoder.encode(location, StandardCharsets.UTF_8);

		String url = "https://us1.locationiq.com/v1/search?key=" + locationIqApiKey + "&q=" + encodedLocation
				+ "&format=json";

		List<Map<String, Object>> locations = restTemplate.getForObject(url, ArrayList.class);

		List<SearchDestionationLocationResponseDto> responseList = new ArrayList<>();

		if (locations != null) {

			for (Map<String, Object> map : locations) {

				SearchDestionationLocationResponseDto responseDto = new SearchDestionationLocationResponseDto();

				responseDto.setAddress(String.valueOf(map.get("display_name")));
				responseDto.setLatitude(Double.parseDouble(String.valueOf(map.get("lat"))));
				responseDto.setLongitude(Double.parseDouble(String.valueOf(map.get("lon"))));

				responseList.add(responseDto);
			}
		}

		return new ResponseStructure<>(HttpStatus.OK.value(), "Locations found", responseList);
	}

	// Select ride and calculate fares
	@SuppressWarnings("unchecked")
	public ResponseStructure<RiderFareResponse> selectRide(CustomerSelectRideDto customerSelectRideDto) {

		if (customerSelectRideDto.getSourceLocation() == null
				|| customerSelectRideDto.getDestinationLocation() == null) {

			throw new IllegalArgumentException("Source and destination coordinates are required");
		}

		double sourceLatitude = customerSelectRideDto.getSourceLocation().getLatitude();

		double sourceLongitude = customerSelectRideDto.getSourceLocation().getLongitude();

		double destinationLatitude = customerSelectRideDto.getDestinationLocation().getLatitude();

		double destinationLongitude = customerSelectRideDto.getDestinationLocation().getLongitude();

		String url = "https://us1.locationiq.com/v1/directions/driving/" + sourceLongitude + "," + sourceLatitude + ";"
				+ destinationLongitude + "," + destinationLatitude + "?key=" + locationIqApiKey
				+ "&steps=true&alternatives=true" + "&geometries=polyline&overview=full";

		Map<String, Object> mapResponse = restTemplate.getForObject(url, Map.class);

		if (mapResponse == null || mapResponse.get("routes") == null) {
			throw new RuntimeException("Route not found");
		}

		List<Map<String, Object>> routes = (List<Map<String, Object>>) mapResponse.get("routes");

		if (routes.isEmpty()) {
			throw new RuntimeException("Route not found");
		}

		Map<String, Object> route = routes.get(0);

		double distanceInMeters = ((Number) route.get("distance")).doubleValue();

		double distanceInKm = distanceInMeters / 1000.0;

		double bikeFare = distanceInKm * getFare(VehicleFare.BIKE);
		double autoFare = distanceInKm * getFare(VehicleFare.AUTO);
		double carFare = distanceInKm * getFare(VehicleFare.CAR);

		RiderFareResponse fareResponse = new RiderFareResponse();

		fareResponse.setPickupAddress(customerSelectRideDto.getSourceAddress());
		fareResponse.setDestinationAddress(customerSelectRideDto.getDestinationAddress());

		fareResponse.setPickupLocation(customerSelectRideDto.getSourceLocation());
		fareResponse.setDestinationLocation(customerSelectRideDto.getDestinationLocation());

		fareResponse.setBikeFare(bikeFare);
		fareResponse.setAutoFare(autoFare);
		fareResponse.setCarFare(carFare);

		// Convert response object into JSON
		String fareJson = objectMapper.writeValueAsString(fareResponse);

		// Store JSON in Redis
		String redisKey = "rideDetails:" + customerSelectRideDto.getCustomerId();

		redisTemplate.opsForValue().set(redisKey, fareJson);

		return new ResponseStructure<>(HttpStatus.OK.value(), "Ride fares calculated successfully", fareResponse);
	}

	// Validate customer OTP
	public ResponseStructure<Boolean> otpValidation(int customerId, String otp) {

		Customer customer = customerServiceRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException());

		if (customer.getOtp() == null || !customer.getOtp().equals(otp)) {

			return new ResponseStructure<>(HttpStatus.BAD_REQUEST.value(), "Invalid OTP", false);
		}

		return new ResponseStructure<>(HttpStatus.OK.value(), "OTP valid", true);
	}

	// Confirm ride and assign rider
	public ResponseStructure<BookingRideResponseDto> confirmRide(ConfirmRideRequestDto confirmRideRequestDto) {

		Customer customer = customerServiceRepository.findById(confirmRideRequestDto.getCustomerId())
				.orElseThrow(() -> new CustomerNotFoundException());

		String redisKey = "rideDetails:" + confirmRideRequestDto.getCustomerId();

		Object redisData = redisTemplate.opsForValue().get(redisKey);

		if (redisData == null) {
			throw new RuntimeException("Ride details not found. Please select a ride first.");
		}

		RiderFareResponse fareResponse = objectMapper.readValue(redisData.toString(), RiderFareResponse.class);

		VehicleFare vehicleType = confirmRideRequestDto.getVehicleType();

		double fare;

		switch (vehicleType) {

		case BIKE:
			fare = fareResponse.getBikeFare();
			break;

		case AUTO:
			fare = fareResponse.getAutoFare();
			break;

		case CAR:
			fare = fareResponse.getCarFare();
			break;

		default:
			throw new IllegalArgumentException("Invalid vehicle type");
		}

		// Create booking
		Booking booking = new Booking();

		booking.setCustomerId(customer.getId());
		booking.setPickupLocation(fareResponse.getPickupAddress());
		booking.setDestinationLocation(fareResponse.getDestinationAddress());

		booking.setSourceLatitude(fareResponse.getPickupLocation().getLatitude());
		booking.setSourceLongitude(fareResponse.getPickupLocation().getLongitude());

		booking.setDestinationLatitude(fareResponse.getDestinationLocation().getLatitude());

		booking.setDestinationLongitude(fareResponse.getDestinationLocation().getLongitude());

		booking.setPaymentType("PENDING");
		booking.setVehicleType(vehicleType.toString());

		booking.setBookingDate(LocalDate.now().toString());
		booking.setBookingTime(LocalTime.now().toString());

		booking.setFare(fare);

		// Find nearby riders
		FindNearByRiderRequestDto riderRequest = new FindNearByRiderRequestDto();

		riderRequest.setVehicleType(vehicleType.toString());

		riderRequest.setLocationCoordinates(new Location(fareResponse.getPickupLocation().getLatitude(),
				fareResponse.getPickupLocation().getLongitude()));

		String riderServiceUrl = "http://localhost:8083/rider/findNearByRiders";

		ResponseEntity<ResponseStructure> riderResponse = restTemplate.postForEntity(riderServiceUrl, riderRequest,
				ResponseStructure.class);

		if (riderResponse.getBody() == null || riderResponse.getBody().getData() == null) {

			throw new RuntimeException("No nearby riders available");
		}

		Object riderData = riderResponse.getBody().getData();

		if (!(riderData instanceof List<?>)) {
			throw new RuntimeException("Unexpected response from Rider Service");
		}

		List<?> riderIds = (List<?>) riderData;

		if (riderIds.isEmpty()) {
			throw new RuntimeException("No nearby riders available");
		}

		int riderId = ((Number) riderIds.get(0)).intValue();

		booking.setRiderId(riderId);

		Booking savedBooking = bookingRepository.save(booking);

		// Store booking details in Redis
		String rideKey = "ride:" + savedBooking.getId();

		redisTemplate.opsForHash().put(rideKey, "bookingId", String.valueOf(savedBooking.getId()));

		redisTemplate.opsForHash().put(rideKey, "customerId", String.valueOf(savedBooking.getCustomerId()));

		redisTemplate.opsForHash().put(rideKey, "pickupLocation", savedBooking.getPickupLocation());

		redisTemplate.opsForHash().put(rideKey, "destinationLocation", savedBooking.getDestinationLocation());

		redisTemplate.opsForHash().put(rideKey, "sourceLatitude", String.valueOf(savedBooking.getSourceLatitude()));

		redisTemplate.opsForHash().put(rideKey, "sourceLongitude", String.valueOf(savedBooking.getSourceLongitude()));

		redisTemplate.opsForHash().put(rideKey, "destinationLatitude",
				String.valueOf(savedBooking.getDestinationLatitude()));

		redisTemplate.opsForHash().put(rideKey, "destinationLongitude",
				String.valueOf(savedBooking.getDestinationLongitude()));

		redisTemplate.opsForHash().put(rideKey, "paymentType", savedBooking.getPaymentType());

		redisTemplate.opsForHash().put(rideKey, "vehicleType", savedBooking.getVehicleType());

		redisTemplate.opsForHash().put(rideKey, "riderId", String.valueOf(savedBooking.getRiderId()));

		redisTemplate.opsForHash().put(rideKey, "bookingDate", savedBooking.getBookingDate());

		redisTemplate.opsForHash().put(rideKey, "bookingTime", savedBooking.getBookingTime());

		redisTemplate.opsForHash().put(rideKey, "fare", String.valueOf(savedBooking.getFare()));

		redisTemplate.opsForHash().put(rideKey, "status", "ASSIGNED");

		// Link booking to rider
		String riderKey = "rider:" + riderId + ":requests";

		redisTemplate.opsForHash().put(riderKey, String.valueOf(savedBooking.getId()), rideKey);

		// Prepare response
		BookingRideResponseDto responseDto = new BookingRideResponseDto();

		responseDto.setCustomerId(savedBooking.getCustomerId());
		responseDto.setRiderId(savedBooking.getRiderId());

		responseDto.setSourceLatitude(savedBooking.getSourceLatitude());
		responseDto.setSourceLongitude(savedBooking.getSourceLongitude());

		responseDto.setDestinationLatitude(savedBooking.getDestinationLatitude());

		responseDto.setDestinationLongitude(savedBooking.getDestinationLongitude());

		responseDto.setFare(savedBooking.getFare());

		return new ResponseStructure<>(HttpStatus.OK.value(), "Ride confirmed successfully", responseDto);
	}

	// Helper to handle missing Redis values and literal "null"
	private String getRedisValue(String rideKey, String field) {

		Object value = redisTemplate.opsForHash().get(rideKey, field);

		if (value == null) {
			return null;
		}

		String result = value.toString().replace("\"", "").trim();

		if (result.isEmpty() || result.equalsIgnoreCase("null")) {
			return null;
		}

		return result;
	}

	// Customer ride history
	public ResponseStructure<List<CustomerRideHistoryRepositoryResponseDto>> customerRideHistory(int customerId) {
		customerServiceRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException());

		List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
		List<CustomerRideHistoryRepositoryResponseDto> rideHistory = new ArrayList<>();

		for (Booking booking : bookings) {
			CustomerRideHistoryRepositoryResponseDto responseDto = new CustomerRideHistoryRepositoryResponseDto();

			responseDto.setBookingId(booking.getId());
			responseDto.setCustomerId(booking.getCustomerId());
			responseDto.setRiderId(booking.getRiderId());

			String rideKey = "ride:" + booking.getId();

			String pickupLocation = getRedisValue(rideKey, "pickupLocation");
			String destinationLocation = getRedisValue(rideKey, "destinationLocation");

			responseDto.setPickupLocation(pickupLocation != null ? pickupLocation : booking.getPickupLocation());
			responseDto.setDestinationLocation(
					destinationLocation != null ? destinationLocation : booking.getDestinationLocation());

			responseDto.setVehicleType(booking.getVehicleType());
			responseDto.setPaymentType(booking.getPaymentType());
			responseDto.setBookingDate(booking.getBookingDate());
			responseDto.setBookingTime(booking.getBookingTime());
			responseDto.setFare(booking.getFare());

			String status = getRedisValue(rideKey, "status");
			responseDto.setStatus(status != null ? status : "NOT_AVAILABLE");

			String pickupTime = getRedisValue(rideKey, "pickupTime");
			String dropTime = getRedisValue(rideKey, "dropTime");

			responseDto.setPickupTime(pickupTime != null ? pickupTime : booking.getPickupTime());
			responseDto.setDropTime(dropTime != null ? dropTime : booking.getDropTime());

			rideHistory.add(responseDto);
		}

		return new ResponseStructure<>(HttpStatus.OK.value(), "Customer ride history fetched successfully",
				rideHistory);
	}
}