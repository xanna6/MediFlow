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

}
