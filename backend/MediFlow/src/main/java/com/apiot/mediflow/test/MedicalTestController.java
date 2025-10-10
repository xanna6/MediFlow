package com.apiot.mediflow.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/medical-tests")
@RestController
public class MedicalTestController {

    private final MedicalTestService medicalTestService;

    public MedicalTestController(MedicalTestService medicalTestService) {
        this.medicalTestService = medicalTestService;
    }

    @GetMapping("/grouped")
    public ResponseEntity<List<MedicalTestGroupDto>> getGroupedMedicalTests() {
        return ResponseEntity.ok(medicalTestService.getGroupedMedicalTests());
    }
}
