package com.Giga_JAD.Wapi_Wapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeStatusResponse {
    private Long bookingId;
    private int statusId;
}