package com.Giga_JAD.Wapi_Wapi.model.blueprint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.Giga_JAD.Wapi_Wapi.model.dao.BookingDAO;
import com.Giga_JAD.Wapi_Wapi.utils.UnauthorizedException;

public class BookingService {
    private final BookingDAO bookingDAO;

    @Autowired
    public BookingService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    @Transactional
    public boolean updateBookingStatus(int bookingId, int workerId) {
        // Verify worker is assigned to this booking
        if (!bookingDAO.isWorkerAssignedToBooking(workerId, bookingId)) {
            throw new UnauthorizedException("Worker not authorized for this booking");
        }

        // Get current status
        int currentStatusId = bookingDAO.getCurrentStatus(bookingId);
        BookingStatus currentStatus = BookingStatus.fromId(currentStatusId);
        
        // Determine next status
        BookingStatus nextStatus;
        switch (currentStatus) {
            case BOOKED:
                nextStatus = BookingStatus.IN_PROGRESS;
                break;
            case IN_PROGRESS:
                nextStatus = BookingStatus.COMPLETED;
                break;
            case COMPLETED:
                throw new IllegalStateException("Booking is already completed");
            default:
                throw new IllegalStateException("Invalid current status");
        }

        // Update the status
        return bookingDAO.updateBookingStatus(bookingId, nextStatus.getStatusId());
    }
}