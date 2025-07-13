package com.apiot.mediflow.referral;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ReferralService {

    private final ReferralRepository referralRepository;

    public List<ReferralDto> getAllReferrals() {
        return referralRepository.findAll()
                .stream()
                .map(this::mapReferralToReferralDto)
                .collect(Collectors.toList());
    }

    private ReferralDto mapReferralToReferralDto(Referral referral) {
       ReferralDto referralDto = new ReferralDto();
       referralDto.setId(referral.getId());
       referralDto.setReferrer(referral.getReferrer());
       referralDto.setReferral_number(referral.getReferral_number());
       referralDto.setCreationDate(referral.getCreationDate());
       return referralDto;
    }

}
