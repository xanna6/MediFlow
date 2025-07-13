package com.apiot.mediflow.referral;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<ReferralDto> createRefferal(@RequestBody ReferralDto referralDto) {
        ReferralDto created = referralService.createReferral(referralDto);
        return ResponseEntity
                .created(URI.create("/api/referrals/" + created.getId()))
                .body(created);
    }
}
