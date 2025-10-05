package com.apiot.mediflow.referralNumberGenerator;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ReferralNumberGenerator {

    private final ReferralNumberSequenceRepository sequenceRepository;

    public ReferralNumberGenerator(ReferralNumberSequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    @Transactional
    public String generateNextNumber() {
        ReferralNumberSequence seq = sequenceRepository.findById(1L)
                .orElseGet(() -> {
                    ReferralNumberSequence newSeq = new ReferralNumberSequence();
                    newSeq.setId(1L);
                    newSeq.setLastNumber(0L);
                    return sequenceRepository.save(newSeq);
                });

        Long next = seq.getLastNumber() + 1;
        seq.setLastNumber(next);
        sequenceRepository.save(seq);

        return String.format("SK%06d", next);
    }
}

