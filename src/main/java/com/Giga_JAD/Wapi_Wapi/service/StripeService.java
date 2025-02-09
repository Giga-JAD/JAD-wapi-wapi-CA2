package com.Giga_JAD.Wapi_Wapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.Giga_JAD.Wapi_Wapi.dto.ProductRequest;
import com.Giga_JAD.Wapi_Wapi.dto.StripeResponse;
import com.Giga_JAD.Wapi_Wapi.model.blueprint.Payment;
import com.Giga_JAD.Wapi_Wapi.model.dao.BookingDAO;
import com.Giga_JAD.Wapi_Wapi.model.dao.PaymentDAO;
import com.Giga_JAD.Wapi_Wapi.model.dao.UserDAO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.util.ArrayList;
import java.util.List;

@Service
public class StripeService {
	@Value("${stripe.secretKey}")
	private String secretKey;

	private final UserDAO userDAO;
	private final PaymentDAO paymentDAO;
	private final BookingDAO bookingDAO; // ✅ Inject BookingDAO

	@Autowired
	public StripeService(PaymentDAO paymentDAO, UserDAO userDAO, BookingDAO bookingDAO) {
		this.paymentDAO = paymentDAO;
		this.userDAO = userDAO;
		this.bookingDAO = bookingDAO; // ✅ Initialize via constructor
	}

	/** ✅ Validate business credentials using Key (username) & Secret (password) */
	public boolean validateBusiness(String key, String secret) {
		return userDAO.validateBusiness(key, secret);
	}

	/** ✅ Create a Stripe Checkout Session and Save Payment */
	public StripeResponse checkoutProducts(List<ProductRequest> productRequests, String Key, String Secret) {
		Stripe.apiKey = secretKey;
		List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

		// ✅ Get recipientId ONCE from the Key (username)
		int recipientId = userDAO.getRecipientIdByKey(Key);

		for (ProductRequest product : productRequests) {
			int bookingId = product.getBookingId();
			String serviceName = bookingDAO.getServiceNameByBookingId(bookingId); // ✅ Fetch correct service name

			// ✅ Create line item for each product
			SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
					.builder().setName(serviceName) // ✅ Use fetched service name
					.build();

			SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
					.setCurrency(product.getCurrency() == null ? "SGD" : product.getCurrency())
					.setUnitAmount(product.getAmount()).setProductData(productData).build();

			SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
					.setQuantity((long) product.getQuantity()) // ✅ Convert long to int
					.setPriceData(priceData).build();

			lineItems.add(lineItem); // ✅ Add all items to a single Stripe session
		}

		try {
			// ✅ Create a SINGLE Stripe Checkout Session for all items
			SessionCreateParams params = SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
					.setSuccessUrl("http://localhost:8080/wapi-wapi/success")
					.setCancelUrl("http://localhost:8080/wapi-wapi/cancel").addAllLineItem(lineItems) // ✅ Add all items
																										// to a single
																										// session
					.build();

			Session session = Session.create(params);

			// ✅ Save a separate payment record per `ProductRequest`
			for (ProductRequest product : productRequests) {
				int bookingId = product.getBookingId();
				String serviceName = bookingDAO.getServiceNameByBookingId(bookingId);

				Payment payment = Payment.builder().recipientId(recipientId) // ✅ Business receiving payment
						.bookingId(bookingId) // ✅ Booking ID associated with the service
						.productName(serviceName) // ✅ Save the correct service name
						.productPrice((int) product.getAmount()).productCurrency(product.getCurrency())
						.productQuantity((int) product.getQuantity()).paymentMethod("Stripe").statusId(10) // PENDING
						.paymentId(session.getId()) // ✅ Store the same Stripe Session ID for all records
						.build();

				paymentDAO.savePayment(payment); // ✅ Save each payment separately
			}

			return StripeResponse.builder().status("Success").message("One Payment Session Created for All Services")
					.sessionId(session.getId()) // ✅ Return a single session ID
					.sessionUrl(session.getUrl()) // ✅ Return a single session URL
					.build();

		} catch (StripeException e) {
			return StripeResponse.builder().status("Failed")
					.message("Error creating payment session: " + e.getMessage()).sessionId(null).sessionUrl(null)
					.build();
		}
	}
}
