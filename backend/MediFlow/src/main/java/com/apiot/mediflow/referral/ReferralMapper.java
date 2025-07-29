package com.apiot.mediflow.referral;

import com.apiot.mediflow.test.MedicalTest;
import com.apiot.mediflow.test.MedicalTestMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class ReferralMapper {

    protected static ReferralDto mapReferralToReferralDto(Referral referral) {
        ReferralDto referralDto = new ReferralDto();
        referralDto.setId(referral.getId());
        referralDto.setReferrer(referral.getReferrer());
        referralDto.setReferral_number(referral.getReferral_number());
        referralDto.setCreationDate(referral.getCreationDate());
        referralDto.setMedicalTestDtoSet(
                referral.getMedicalTests().stream()
                        .map(MedicalTestMapper::mapMedicalTestToMedicalTestDto)
                        .collect(Collectors.toSet()));
        return referralDto;
    }

    protected static Referral mapReferralCreateDtoToReferral(ReferralCreateDto referralRequestDto, Set<MedicalTest> medicalTests) {
        Referral referral = new Referral();
        referral.setReferrer(referralRequestDto.getReferrer());
        referral.setReferral_number(referralRequestDto.getReferralNumber());
        referral.setMedicalTests(medicalTests);
        return referral;
    }
}
