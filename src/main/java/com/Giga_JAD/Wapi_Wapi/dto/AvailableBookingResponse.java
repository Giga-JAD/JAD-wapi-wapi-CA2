package com.Giga_JAD.Wapi_Wapi.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AvailableBookingResponse {
    private int serviceId;
    private String date;
    private List<TimeSlotRequest> availableTimeSlots;
}