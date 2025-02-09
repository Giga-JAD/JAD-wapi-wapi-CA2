package com.Giga_JAD.Wapi_Wapi.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Giga_JAD.Wapi_Wapi.model.blueprint.Booking;
import com.Giga_JAD.Wapi_Wapi.model.blueprint.BookingService;
import com.Giga_JAD.Wapi_Wapi.utils.UnauthorizedException;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
   

    // GET /bookings/{bookingId}
    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long bookingId) {
        return bookingService.getBookingById(bookingId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /bookings/{bookingId}/status
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<Booking> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam Integer statusId) {

        return bookingService.updateBookingStatus(bookingId, statusId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable int bookingId,
            @RequestHeader("Worker-ID") int workerId) {
        try {
            boolean updated = bookingService.updateBookingStatus(bookingId, workerId);
            if (updated) {
                return ResponseEntity.ok().body(new HashMap<String, String>() {{
                    put("message", "Status updated successfully");
                }});
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        }
    }
}