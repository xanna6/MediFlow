package com.apiot.mediflow.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableSlotsResponse {

    private Long pointId;
    private String date;
    private List<String> availableSlots;
}
