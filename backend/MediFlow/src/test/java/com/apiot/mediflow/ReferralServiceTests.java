package com.apiot.mediflow;

import com.apiot.mediflow.referral.Referral;
import com.apiot.mediflow.referral.ReferralDto;
import com.apiot.mediflow.referral.ReferralRepository;
import com.apiot.mediflow.referral.ReferralService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ReferralServiceTests {

    private ReferralRepository referralRepository;
    private ReferralService referralService;

    @BeforeEach
    void setup() {
        referralRepository = mock(ReferralRepository.class);
        referralService = new ReferralService(referralRepository);
    }

    @Test
    void shouldReturnReferrals() {
        // given
        List<Referral> referralList = List.of(
                new Referral(1L, "Jan Kowalski", "A25000001", LocalDateTime.now()),
                new Referral(2L, "Jakub Kozłowski", "A25000002", LocalDateTime.now())
        );

        when(referralRepository.findAll()).thenReturn(referralList);

        // when
        List<ReferralDto> result = referralService.getAllReferrals();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Jan Kowalski", result.get(0).getReferrer());
        assertEquals("Jakub Kozłowski", result.get(1).getReferrer());
        assertEquals("A25000001", result.get(0).getReferral_number());
        assertEquals("A25000002", result.get(1).getReferral_number());

        verify(referralRepository, times(1)).findAll();
    }
}
