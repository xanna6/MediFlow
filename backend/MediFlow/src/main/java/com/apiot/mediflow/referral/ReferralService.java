package com.apiot.mediflow.referral;

import com.apiot.mediflow.test.MedicalTest;
import com.apiot.mediflow.test.MedicalTestRepository;
import jakarta.persistence.EntityNotFoundException;
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

     public ReferralDto createReferral(ReferralCreateDto referralCreateDto) {
         Set<MedicalTest> tests = new HashSet<>(medicalTestRepository
                 .findAllById(referralCreateDto.getMedicalTestIds()));
         Referral referral = ReferralMapper.mapReferralCreateDtoToReferral(referralCreateDto, tests);
         return ReferralMapper.mapReferralToReferralDto(referralRepository.save(referral));
     }

}
