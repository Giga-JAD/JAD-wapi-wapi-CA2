package com.Giga_JAD.Wapi_Wapi.controller;

import org.springframework.web.bind.annotation.RestController;
import com.Giga_JAD.Wapi_Wapi.service.StripeService;
import com.Giga_JAD.Wapi_Wapi.service.dto.ProductRequest;
import com.Giga_JAD.Wapi_Wapi.service.dto.StripeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/product/v1")
public class PaymentController {

	private StripeService stripeService;

	public PaymentController(StripeService stripeService) {
		this.stripeService = stripeService;
	}

	@PostMapping("/checkout")
	public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody ProductRequest productRequest) {
		StripeResponse stripeResponse = stripeService.checkoutProducts(productRequest);
		return ResponseEntity.status(HttpStatus.OK).body(stripeResponse);
	}
}
