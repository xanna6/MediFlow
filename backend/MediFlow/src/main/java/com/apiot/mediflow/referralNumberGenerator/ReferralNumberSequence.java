package com.apiot.mediflow.referralNumberGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ReferralNumberSequence {

    @Id
    private Long id = 1L;

    private Long lastNumber;
}
