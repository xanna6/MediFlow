package com.apiot.mediflow.referral;

import com.apiot.mediflow.test.MedicalTest;
import com.apiot.mediflow.test.MedicalTestMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class ReferralMapper {

    protected static ReferralDto mapReferralToReferralDto(Referral referral) {
        ReferralDto referralDto = new ReferralDto();
        referralDto.setId(referral.getId());
        referralDto.setReferrerFirstname(referral.getDoctor().getFirstname());
        referralDto.setReferrerLastname(referral.getDoctor().getLastname());
        referralDto.setReferrerSpecialization(referral.getDoctor().getSpecialization());
        referralDto.setReferralNumber(referral.getReferralNumber());
        referralDto.setCreationDate(referral.getCreationDate());
        referralDto.setMedicalTestDtoSet(
                referral.getMedicalTests().stream()
                        .map(MedicalTestMapper::mapMedicalTestToMedicalTestDto)
                        .collect(Collectors.toSet()));
        return referralDto;
    }

    protected static Referral mapReferralCreateDtoToReferral(ReferralCreateDto referralRequestDto, Set<MedicalTest> medicalTests) {
        Referral referral = new Referral();
        referral.setReferralNumber(referralRequestDto.getReferralNumber());
        referral.setMedicalTests(medicalTests);
        return referral;
    }
}
