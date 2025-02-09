package com.Giga_JAD.Wapi_Wapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Giga_JAD.Wapi_Wapi.service.BookingStatusService;
import com.Giga_JAD.Wapi_Wapi.dto.RealTimeStatusResponse;
import com.Giga_JAD.Wapi_Wapi.dto.ErrorResponse;

@RestController
@RequestMapping("/realtime/status")
public class RealTimeStatusController {
    private final BookingStatusService bookingStatusService;

    @Autowired
    public RealTimeStatusController(BookingStatusService bookingStatusService) {
        this.bookingStatusService = bookingStatusService;
    }

    @PutMapping("/update/{bookingId}")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long bookingId) {
        try {
            int updatedStatus = bookingStatusService.updateBookingStatus(bookingId);
            return ResponseEntity.ok(new RealTimeStatusResponse(bookingId, updatedStatus));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}