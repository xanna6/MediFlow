package com.apiot.mediflow.referral;

import com.apiot.mediflow.users.PatientDto;
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
    private PatientDto patientDto;

    @NotEmpty
    private Set<Long> medicalTestIds;
}
