package com.Giga_JAD.Wapi_Wapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Giga_JAD.Wapi_Wapi.model.dao.BookingDAO;

@Service
public class BookingStatusService {
    private final BookingDAO bookingDAO;

    @Autowired
    public BookingStatusService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    @Transactional
    public int updateBookingStatus(Long bookingId) {
        // Get current status
        int currentStatus = bookingDAO.getBookingStatus(bookingId);
        
//        // Validate status is between 6 (Booked) and 8 (Completed)
//        if (currentStatus < 6 || currentStatus >= 8) {
//            throw new IllegalStateException("Invalid status transition");
//        }

        // Increment status
        int newStatus = currentStatus + 1;
        bookingDAO.updateStatus(bookingId, newStatus);

        return newStatus;
    }
}