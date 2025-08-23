package com.apiot.mediflow.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDto {
    private Long id;
    private LocalDateTime date;
    private Long referralId;
    private Long collectionPointId;

    public AppointmentResponseDto(Appointment appointment) {
        this.id = appointment.getId();
        this.date = appointment.getDate();
        this.referralId = appointment.getReferral().getId();
        this.collectionPointId = appointment.getCollectionPoint().getId();
    }
}
