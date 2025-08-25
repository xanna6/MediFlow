package com.apiot.mediflow.barcodeGenerator;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class SampleCodeGenerator {

    private final SampleCodeSequenceRepository repository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public SampleCodeGenerator(SampleCodeSequenceRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String generateSampleCode() {
        String today = LocalDate.now().format(formatter);

        SampleCodeSequence barcodeSequence = repository.findById(today)
                .orElseGet(() -> {
                    SampleCodeSequence s = new SampleCodeSequence();
                    s.setDate(today);
                    s.setCounter(0);
                    return s;
                });

        barcodeSequence.setCounter(barcodeSequence.getCounter() + 1);
        repository.save(barcodeSequence);

        return "SMP-" + today + "-" + String.format("%04d", barcodeSequence.getCounter());
    }
}
