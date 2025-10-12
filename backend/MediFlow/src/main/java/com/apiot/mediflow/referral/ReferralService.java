package com.apiot.mediflow.referral;

import com.apiot.mediflow.referralNumberGenerator.ReferralNumberGenerator;
import com.apiot.mediflow.test.MedicalTest;
import com.apiot.mediflow.test.MedicalTestRepository;
import com.apiot.mediflow.users.Doctor;
import com.apiot.mediflow.users.DoctorRepository;
import com.apiot.mediflow.users.Patient;
import com.apiot.mediflow.users.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.apiot.mediflow.referral.ReferralMapper.*;

@AllArgsConstructor
@Service
public class ReferralService {

    private final ReferralRepository referralRepository;
    private final MedicalTestRepository medicalTestRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ReferralNumberGenerator referralNumberGenerator;

    public List<ReferralDto> getAllReferrals() {
        return referralRepository.findAllWithMedicalTests()
                .stream()
                .map(ReferralMapper::mapReferralToReferralDto)
                .collect(Collectors.toList());
    }
     public ReferralDto getReferralById(long id) {
        Referral referral = referralRepository.findByIdWithMedicalTests(id)
                .orElseThrow(() -> new EntityNotFoundException("Referral with id " + id + " not found"));
        return mapReferralToReferralDto(referral);
     }

     @Transactional
     public ReferralDto createReferral(ReferralCreateDto referralCreateDto) {

         Referral referral = new Referral();
         referral.setReferralNumber(referralNumberGenerator.generateNextNumber());

         Set<MedicalTest> tests = new HashSet<>(medicalTestRepository
                 .findAllById(referralCreateDto.getMedicalTestIds()));
         
         referral.setMedicalTests(tests);

         Doctor doctor = doctorRepository.findById(referralCreateDto.getDoctorId())
                 .orElseThrow(() -> new EntityNotFoundException("Doctor with id " + referralCreateDto.getDoctorId() + " not found"));

         Patient patient = patientRepository.findByPesel(referralCreateDto.getPatientDto().getPesel())
                 .orElseGet(() -> patientRepository.save(new Patient(
                         referralCreateDto.getPatientDto().getFirstName(),
                         referralCreateDto.getPatientDto().getLastName(),
                         referralCreateDto.getPatientDto().getPesel(),
                         referralCreateDto.getPatientDto().getBirthDate()
                 )));

         referral.setDoctor(doctor);
         referral.setPatient(patient);
         Referral savedReferral = referralRepository.save(referral);

         return ReferralMapper.mapReferralToReferralDto(savedReferral);
     }

}
