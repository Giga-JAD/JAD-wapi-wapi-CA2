package com.Giga_JAD.Wapi_Wapi.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.Giga_JAD.Wapi_Wapi.dto.AvailableBookingResponse;
import com.Giga_JAD.Wapi_Wapi.dto.ServiceRequest;
import com.Giga_JAD.Wapi_Wapi.model.dao.BookingDAO;
import com.Giga_JAD.Wapi_Wapi.model.dao.UserDAO;
import com.Giga_JAD.Wapi_Wapi.service.ServiceManagementService;
//import com.Giga_JAD.Wapi_Wapi.model.dao.UserDAO;

import java.util.List;
import com.Giga_JAD.Wapi_Wapi.model.dao.ServiceRequestDAO;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/booking")
public class ServiceController {
	@Autowired
    private UserDAO userDAO;
    
    @Autowired
    private BookingDAO BookingDAO;
    private final ServiceManagementService serviceManagementService;

    public ServiceController(UserDAO userDAO, ServiceManagementService serviceManagementService) {
        this.userDAO = userDAO;
        this.serviceManagementService = serviceManagementService;
    }

	@GetMapping("/services")
	public ResponseEntity<?> getAllServices(
			@RequestHeader("X-Username") String username,
			@RequestHeader("X-Secret") String secret) {
		
		// ✅ Validate business credentials
		try {
			if(!userDAO.validateBusiness(username, secret)) {
				return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
		}
		
		try {
			// Use serviceManagementService to get the list of services
            List<ServiceRequest> services = serviceManagementService.getAllServices();
            return ResponseEntity.ok(services);
            
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
		}
	}
	
	@GetMapping("/available-slots/{serviceId}")
    public ResponseEntity<?> getAvailableBookings(
            @PathVariable int serviceId,
            @RequestHeader("X-Username") String username,
            @RequestHeader("X-Secret") String secret) {

        try {
            if (!userDAO.validateBusiness(username, secret)) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
            }
            
            List<AvailableBookingResponse> availableSlots = BookingDAO.getAvailableBookings(serviceId);
            return ResponseEntity.ok(availableSlots);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

}












