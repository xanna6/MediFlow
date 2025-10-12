package com.apiot.mediflow.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/patients")
@RestController
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping("/by-pesel/{pesel}")
    public ResponseEntity<PatientDto> findByPesel(@PathVariable String pesel) {
        return patientRepository.findByPesel(pesel)
                .map(patient -> new PatientDto(
                        patient.getFirstName(),
                        patient.getLastName(),
                        patient.getPesel(),
                        patient.getBirthDate()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
