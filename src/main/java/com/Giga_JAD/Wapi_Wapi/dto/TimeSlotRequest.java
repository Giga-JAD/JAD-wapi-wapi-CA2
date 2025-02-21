package com.Giga_JAD.Wapi_Wapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeSlotRequest {
    private int timeSlotId;
    private int serviceTimeSlotId;
    private String timeSlot;
    private boolean isAvailable;
}