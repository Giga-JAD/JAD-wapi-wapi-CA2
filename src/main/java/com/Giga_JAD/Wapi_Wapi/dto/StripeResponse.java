package com.Giga_JAD.Wapi_Wapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder // Works properly now
public class StripeResponse {
	private String status;
	private String message;
	private String sessionId;
	private String sessionUrl;
}
