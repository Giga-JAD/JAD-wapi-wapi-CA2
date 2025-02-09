package com.Giga_JAD.Wapi_Wapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProviderSelectionRequest {
    private int serviceTimeSlotId;
}