package com.apiot.mediflow.referral;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/referrals")
@RestController
public class ReferralController {

    private ReferralService referralService;

    @GetMapping
    public ResponseEntity<List<ReferralDto>> getAllReferrals() {
        return ResponseEntity.ok(referralService.getAllReferrals());
    }
}
