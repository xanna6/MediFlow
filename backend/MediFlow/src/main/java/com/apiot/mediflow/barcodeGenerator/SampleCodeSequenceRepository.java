package com.apiot.mediflow.barcodeGenerator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleCodeSequenceRepository extends JpaRepository<SampleCodeSequence, String> {
}
