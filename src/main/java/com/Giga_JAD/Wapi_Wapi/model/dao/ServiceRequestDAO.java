package com.Giga_JAD.Wapi_Wapi.model.dao;

import com.Giga_JAD.Wapi_Wapi.dto.ServiceRequest;
import com.Giga_JAD.Wapi_Wapi.service.ServiceManagementService;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServiceRequestDAO implements ServiceManagementService {
    private final JdbcTemplate jdbcTemplate;

    public ServiceRequestDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ServiceRequest> getAllServices() {
        String query = """
	        		SELECT s.service_id, service_name, price, duration_in_hour, service_description, category_name
					FROM service s 
					LEFT JOIN category c 
					ON s.category_id = c.category_id;
        		""";
        
        return jdbcTemplate.query(query, (rs, rowNum) ->
            ServiceRequest.builder()
                .serviceId(rs.getInt("service_id"))
                .categoryName(rs.getString("category_name"))
                .serviceName(rs.getString("service_name"))
                .price(rs.getDouble("price"))
                .durationInHour(rs.getInt("duration_in_hour"))
                .build()
        );
    }
}