package com.Giga_JAD.Wapi_Wapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.Giga_JAD.Wapi_Wapi.service.dto.ProductRequest;
import com.Giga_JAD.Wapi_Wapi.service.dto.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service

public class StripeService {
	@Value("${stripe.secretKey}")
	private String secretKey;

	public StripeResponse checkoutProducts(ProductRequest productRequest) {
		Stripe.apiKey = secretKey;

		SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
				.builder().setName(productRequest.getName()).build();
		
		SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
				.setCurrency(productRequest.getCurrency()==null ? "USD" : productRequest.getCurrency())
				.setUnitAmount(productRequest.getAmount())
				.setProductData(productData)
				.build();
		
		SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
				.setQuantity(productRequest.getQuantity()).setPriceData(priceData).build();
		
		SessionCreateParams params = SessionCreateParams.builder()
		.setMode(SessionCreateParams.Mode.PAYMENT)
		.setSuccessUrl("http://localhost:8080/JAD-wapi-wapi/success")
		.setCancelUrl("http://localhost:8080/JAD-wapi-wapi/cancel")
		.addLineItem(lineItem)
		.build();
		
		Session session = null;
		try {
			session = Session.create(params);
		}catch(StripeException e) {
				// Handle error
            e.printStackTrace();;
		}
		return StripeResponse.builder()
				.status("Success")
				.message("Payment Session Created")
				.sessionId(session.getId())
				.sessionUrl(session.getUrl())
				.build();
	}
}
