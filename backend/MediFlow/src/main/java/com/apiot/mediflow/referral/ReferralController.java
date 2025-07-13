package com.apiot.mediflow.referral;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity<ReferralDto> getReferralById(@PathVariable Long id) {
        return ResponseEntity.ok(referralService.getReferralById(id));
    }
}
