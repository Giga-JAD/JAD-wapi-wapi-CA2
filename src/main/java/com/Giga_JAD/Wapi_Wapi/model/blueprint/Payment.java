package com.Giga_JAD.Wapi_Wapi.model.blueprint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
	private int recipientId; // Business receiving the payment
	private int bookingId; // Booking associated with payment
	private String productName;
	private int productPrice;
	private String productCurrency;
	private int productQuantity;
	private String paymentMethod; // e.g., "Stripe"
	private int statusId; // "PENDING", "COMPLETED"
	private String paymentId;
}
