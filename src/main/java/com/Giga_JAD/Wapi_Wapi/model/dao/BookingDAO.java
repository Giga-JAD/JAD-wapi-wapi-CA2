package com.Giga_JAD.Wapi_Wapi.model.dao;

import com.Giga_JAD.Wapi_Wapi.dto.ServiceRequest;
import com.Giga_JAD.Wapi_Wapi.dto.AvailableBookingResponse;
import com.Giga_JAD.Wapi_Wapi.dto.TimeSlotRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

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
	
	public List<AvailableBookingResponse> getAvailableBookings(int serviceId) {
        LocalDate today = LocalDate.now();
        LocalDate twoWeeksFromNow = today.plusWeeks(2);
        
        String sql = """
            WITH DailyBookings AS (
                SELECT 
                    st.service_id,
                    st.service_timeslot_date,
                    COUNT(st.timeslot_id) as slots_count
                FROM SERVICE_TIMESLOT st
                WHERE st.service_id = ?
                AND st.service_timeslot_date BETWEEN ? AND ?
                GROUP BY st.service_id, st.service_timeslot_date
            ),
            TimeslotBookings AS (
                SELECT 
                    t.timeslot_id,
                    CASE WHEN t."8am-9am" IS NOT NULL THEN 1 ELSE 0 END +
                    CASE WHEN t."9am-10am" IS NOT NULL THEN 1 ELSE 0 END +
                    CASE WHEN t."10am-11am" IS NOT NULL THEN 1 ELSE 0 END +
                    CASE WHEN t."11am-12pm" IS NOT NULL THEN 1 ELSE 0 END +
                    CASE WHEN t."1pm-2pm" IS NOT NULL THEN 1 ELSE 0 END +
                    CASE WHEN t."2pm-3pm" IS NOT NULL THEN 1 ELSE 0 END +
                    CASE WHEN t."4pm-5pm" IS NOT NULL THEN 1 ELSE 0 END +
                    CASE WHEN t."5pm-6pm" IS NOT NULL THEN 1 ELSE 0 END as booked_slots_count
                FROM TIMESLOT t
            )
            SELECT 
                st.service_timeslot_id,
                st.service_id,
                st.service_timeslot_date,
                t.timeslot_id,
                t."8am-9am", t."9am-10am", t."10am-11am", t."11am-12pm",
                t."1pm-2pm", t."2pm-3pm", t."4pm-5pm", t."5pm-6pm",
                st.taken_by_user_id
            FROM DailyBookings db
            JOIN SERVICE_TIMESLOT st ON db.service_id = st.service_id 
                AND db.service_timeslot_date = st.service_timeslot_date
            JOIN TIMESLOT t ON st.timeslot_id = t.timeslot_id
            JOIN TimeslotBookings tb ON t.timeslot_id = tb.timeslot_id
            WHERE 
                st.taken_by_user_id IS NULL
                AND (
                    db.slots_count > 1
                    OR (
                        db.slots_count = 1
                        AND tb.booked_slots_count > 0
                    )
                )
            ORDER BY st.service_timeslot_date, t.timeslot_id
        """;

        return jdbcTemplate.query(sql, 
            (rs, rowNum) -> {
                List<TimeSlotRequest> availableSlots = new ArrayList<>();
                
                String[] timeSlots = {
                    "8am-9am", "9am-10am", "10am-11am", "11am-12pm",
                    "1pm-2pm", "2pm-3pm", "4pm-5pm", "5pm-6pm"
                };
                
                for (String timeSlot : timeSlots) {
                    Integer userId = rs.getObject(timeSlot, Integer.class);
                    if (userId != null) {
                        availableSlots.add(TimeSlotRequest.builder()
                            .timeSlotId(rs.getInt("timeslot_id"))
                            .serviceTimeSlotId(rs.getInt("service_timeslot_id"))
                            .timeSlot(timeSlot)
                            .isAvailable(true)
                            .build());
                    }
                }
                
                return AvailableBookingResponse.builder()
                    .serviceId(rs.getInt("service_id"))
                    .date(rs.getString("service_timeslot_date"))
                    .availableTimeSlots(availableSlots)
                    .build();
            },
            serviceId, today, twoWeeksFromNow);
    }

	public boolean selectServiceTimeSlot(int serviceTimeSlotId, String username) {
        // First get the user_id for the service provider
        String getUserIdSql = "SELECT user_id FROM users WHERE LOWER(username) = LOWER(?)";
        
        try {
            Integer userId = jdbcTemplate.queryForObject(getUserIdSql, Integer.class, username);
            
            if (userId == null) {
                throw new IllegalArgumentException("Service provider not found");
            }

            // Update the service_timeslot table
            String updateSql = """
                UPDATE SERVICE_TIMESLOT 
                SET taken_by_user_id = ? 
                WHERE service_timeslot_id = ? 
                AND taken_by_user_id IS NULL
                """;

            int rowsAffected = jdbcTemplate.update(updateSql, userId, serviceTimeSlotId);
            
            return rowsAffected > 0;
            
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Service provider not found");
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Error processing selection: " + e.getMessage());
        }
    }
}
