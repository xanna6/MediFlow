package com.apiot.mediflow.referral;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ReferralCreateDto {

    private String referrer;
    private String referralNumber;
    private Set<Long> medicalTestIds;
}
