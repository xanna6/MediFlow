package com.apiot.mediflow.sample;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {

    @Query("""
    SELECT s
    FROM Sample s
    WHERE s.sampleCode = :sampleCode
      AND s.appointment.referral.patient.pesel = :pesel
    """)
    Optional<Sample> findByPatientPeselAndSampleCode(
            @Param("pesel") String pesel,
            @Param("sampleCode") String sampleCode);
}
