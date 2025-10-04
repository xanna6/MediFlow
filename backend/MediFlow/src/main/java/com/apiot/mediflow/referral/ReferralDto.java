package com.apiot.mediflow.referral;

import com.apiot.mediflow.test.MedicalTestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReferralDto {

    private Long id;
    private String referrerFirstname;
    private String referrerLastname;
    private String referrerSpecialization;
    private String referralNumber;
    private LocalDateTime creationDate;
    private Set<MedicalTestDto> medicalTestDtoSet;
}
