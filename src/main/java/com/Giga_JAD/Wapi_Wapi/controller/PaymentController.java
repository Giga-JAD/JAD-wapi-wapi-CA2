package com.Giga_JAD.Wapi_Wapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Giga_JAD.Wapi_Wapi.dto.ProductRequest;
import com.Giga_JAD.Wapi_Wapi.dto.StripeResponse;
import com.Giga_JAD.Wapi_Wapi.model.blueprint.Payment;
import com.Giga_JAD.Wapi_Wapi.model.dao.PaymentDAO;
import com.Giga_JAD.Wapi_Wapi.service.StripeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

@RestController
@RequestMapping("/payments")
public class PaymentController {

	private final StripeService stripeService;
	private final ObjectMapper objectMapper;
	private final PaymentDAO paymentDAO;

	public PaymentController(StripeService stripeService, ObjectMapper objectMapper, PaymentDAO paymentDAO) {
		this.stripeService = stripeService;
		this.objectMapper = objectMapper;
		this.paymentDAO = paymentDAO;
	}

	/** ✅ Create Payment (Checkout Session) */
	@PostMapping("/checkout")
	public StripeResponse createCheckoutSession(@RequestHeader("X-Username") String username,
			@RequestHeader("X-Secret") String secret, @RequestHeader("X-third-party") boolean thirdParty,
			@RequestBody List<ProductRequest> products) {

		// ✅ Validate business credentials
		if (!stripeService.validateBusiness(username, secret, thirdParty)) {
			throw new IllegalArgumentException("Invalid business credentials!");
		}

		return stripeService.checkoutProducts(products, username, secret);
	}

	/** ✅ Fetch Payment Details */
	@GetMapping("/{bookingId}")
	public ResponseEntity<Map<String, Object>> getPaymentDetails(@RequestHeader("X-Username") String Key,
			@RequestHeader("X-Secret") String Secret, @RequestHeader("X-third-party") boolean thirdParty,
			@PathVariable String bookingId) {

		// ✅ Validate Credentials
		try {
			if (!stripeService.validateBusiness(Key, Secret, thirdParty)) {
				return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
		}

		try {
			stripeService.initialize(); // Ensure Stripe API key is set

			// 🟢 Retrieve payment details using bookingId
			Payment payment = paymentDAO.getPaymentByBookingId(bookingId); // Fetch from DB
			if (payment == null) {
				return ResponseEntity.status(404).body(Map.of("error", "Payment not found for this booking"));
			}

			String paymentId = payment.getPaymentId(); // Retrieve associated paymentId

			// 🟢 Retrieve Stripe session details using the paymentId
			Session session = Session.retrieve(paymentId);
			Map<String, Object> stripeDetails = objectMapper.readValue(session.toJson(), new TypeReference<>() {
			});

			// 🟢 Merge Stripe and DB payment details
			Map<String, Object> response = new HashMap<>(stripeDetails);
			response.put("paymentId", payment.getPaymentId());
			response.put("recipientId", payment.getRecipientId());
			response.put("bookingId", payment.getBookingId());
			response.put("productName", payment.getProductName());
			response.put("paymentMethod", payment.getPaymentMethod());
			response.put("statusId", payment.getStatusId());

			return ResponseEntity.ok(response);

		} catch (StripeException | JsonProcessingException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}
}
