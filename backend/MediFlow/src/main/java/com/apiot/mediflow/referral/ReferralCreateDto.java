package com.apiot.mediflow.referral;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ReferralCreateDto {

    @NotNull
    private Long doctorId;

    @NotNull
    private Long patientId;

    @NotBlank
    private String referralNumber;

    @NotEmpty
    private Set<Long> medicalTestIds;
}
