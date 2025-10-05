package com.apiot.mediflow.referralNumberGenerator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralNumberSequenceRepository extends JpaRepository<ReferralNumberSequence, Long> {
}
