package com.apiot.mediflow.referral;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Long> {

    @Query("SELECT DISTINCT r FROM Referral r LEFT JOIN FETCH r.medicalTests")
    List<Referral> findAllWithMedicalTests();

    @Query("SELECT r FROM Referral r LEFT JOIN FETCH r.medicalTests WHERE r.id = :id")
    Optional<Referral> findByIdWithMedicalTests(@Param("id") Long id);

    Optional<Referral> findByReferralNumber(String referralNumber);
}
