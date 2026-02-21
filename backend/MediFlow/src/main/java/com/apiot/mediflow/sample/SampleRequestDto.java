package com.apiot.mediflow.sample;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleRequestDto {

    @NotNull
    private Long appointmentId;

    @NotNull
    private Long labEmployeeId;
}
