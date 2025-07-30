package com.apiot.mediflow.referral;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ReferralCreateDto {

    @NotBlank
    private String referrer;

    @NotBlank
    private String referralNumber;

    @NotEmpty
    private Set<Long> medicalTestIds;
}
