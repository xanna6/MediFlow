package com.apiot.mediflow.referral;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReferralDto {

    private Long id;
    private String referrer;
    private String referral_number;
    private LocalDateTime creationDate;
}
