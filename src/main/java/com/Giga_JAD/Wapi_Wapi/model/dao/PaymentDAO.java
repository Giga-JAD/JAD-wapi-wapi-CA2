package com.Giga_JAD.Wapi_Wapi.model.dao;

import com.Giga_JAD.Wapi_Wapi.model.blueprint.Payment;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class PaymentDAO {

	private final JdbcTemplate jdbcTemplate;

	// Constructor-based dependency injection
	public PaymentDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// ✅ CREATE - Insert a new payment record
	public boolean savePayment(Payment payment) {
		String sql = "INSERT INTO payment (recipient_id, booking_id, product_name, product_price, product_currency, product_quantity, payment_method, payment_status, payment_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			jdbcTemplate.update(sql, payment.getRecipientId(), payment.getBookingId(), payment.getProductName(),
					payment.getProductPrice(), payment.getProductCurrency(), payment.getProductQuantity(),
					payment.getPaymentMethod(), payment.getStatusId(), payment.getPaymentId());

			return true; // ✅ Payment saved successfully
		} catch (DuplicateKeyException e) {
			System.out.println("⚠️ Duplicate payment detected for Payment ID: " + payment.getPaymentId());
			return false; // ✅ Indicate that payment was not inserted
		} catch (Exception e) {
			e.printStackTrace();
			return false; // ✅ Handle other unexpected errors
		}
	}

	// ✅ READ - Get payment by ID
	public Payment getPaymentByBookingId(String booking_id) {
		String sql = "SELECT * FROM payment WHERE booking_id = ?";

		List<Payment> payments = jdbcTemplate.query(sql, paymentRowMapper, booking_id);

		return payments.stream().findFirst().orElse(null); // ✅ Return first result or null if no match found
	}

	// ✅ READ - Get all payments
	public List<Payment> getAllPayments() {
		String sql = "SELECT * FROM payment";
		return jdbcTemplate.query(sql, paymentRowMapper);
	}

	// ✅ UPDATE - Update payment status
	public void updatePaymentStatus(int status_id, String payment_id) {
		String sql = "UPDATE payment SET payment_status = ? WHERE payment_id = ?";
		jdbcTemplate.update(sql, status_id, payment_id);
	}

	// ✅ DELETE - Delete payment by ID
	public void deletePayment(String payment_id) {
		String sql = "DELETE FROM payment WHERE recipient_id = ? AND booking_id = ?";
		jdbcTemplate.update(sql, payment_id);
	}

	// RowMapper for mapping SQL result set to Payment object
	private final RowMapper<Payment> paymentRowMapper = (rs, rowNum) -> new Payment(rs.getInt("recipient_id"),
			rs.getInt("booking_id"), rs.getString("product_name"), rs.getInt("product_price"),
			rs.getString("product_currency"), rs.getInt("product_quantity"), // ✅ Fixed: Ensure it's an int
			rs.getString("payment_method"), // ✅ Make sure this is stored as a String
			rs.getInt("payment_status"), rs.getString("payment_id"));

}
