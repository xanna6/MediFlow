package com.apiot.mediflow;

import com.apiot.mediflow.referral.Referral;
import com.apiot.mediflow.referral.ReferralDto;
import com.apiot.mediflow.referral.ReferralRepository;
import com.apiot.mediflow.referral.ReferralService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void shouldReturnReferralById() {
        // given
        Referral referral = new Referral(1L, "Jan Kowalski", "A25000001", LocalDateTime.now());

        when(referralRepository.findById(1L)).thenReturn(Optional.of(referral));

        // when
        ReferralDto result = referralService.getReferralById(1L);

        // then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Jan Kowalski", result.getReferrer());
        assertEquals("A25000001", result.getReferral_number());

        verify(referralRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenReferralNotFound() {
        // given
        when(referralRepository.findById(123L)).thenReturn(Optional.empty());

        // when + then
        assertThrows(EntityNotFoundException.class, () -> referralService.getReferralById(123L));
    }

    @Test
    void shouldCreateReferral() {
        // given
        ReferralDto referralDto = new ReferralDto(null, "Jan Kowalski", "A25000003", null);
        Referral savedReferral = new Referral(1L, "Jan Kowalski", "A25000003", LocalDateTime.now());
        when(referralRepository.save(any(Referral.class))).thenReturn(savedReferral);

        // when
        ReferralDto result = referralService.createReferral(referralDto);

        // then
        assertNotNull(result.getId());
        assertEquals(savedReferral.getReferrer(), result.getReferrer());
        assertEquals(savedReferral.getReferral_number(), result.getReferral_number());
        verify(referralRepository, times(1)).save(any(Referral.class));
    }
}
