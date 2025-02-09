package com.Giga_JAD.Wapi_Wapi.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookingDAO {
	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public BookingDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/** ✅ Get service name from booking_id */
	public String getServiceNameByBookingId(int bookingId) {
		String sql = """
				    SELECT s.service_name
				    FROM booking b
				    JOIN service s ON b.service_id = s.service_id
				    WHERE b.booking_id = ?
				""";

		try {
			return jdbcTemplate.queryForObject(sql, String.class, bookingId);
		} catch (EmptyResultDataAccessException e) {
			throw new IllegalArgumentException("No service found for bookingId: " + bookingId);
		}
	}

    public boolean updateBookingStatus(int bookingId, int newStatusId) {
        String sql = "UPDATE booking SET status_id = ? WHERE booking_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, newStatusId, bookingId);
        return rowsAffected > 0;
    }

    public int getCurrentStatus(int bookingId) {
        String sql = "SELECT status_id FROM booking WHERE booking_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, bookingId);
    }

    public boolean isWorkerAssignedToBooking(int workerId, int bookingId) {
        String sql = "SELECT COUNT(*) FROM booking WHERE booking_id = ? AND worker_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, bookingId, workerId);
        return count > 0;
    }
}
