package com.Giga_JAD.Wapi_Wapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // Works properly now
public class ServiceRequest {
	private int serviceId;
	private int categoryId;
	private String serviceName;
	private double price;
	private int durationInHour;
}
