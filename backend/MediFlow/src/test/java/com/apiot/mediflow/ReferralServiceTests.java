package com.apiot.mediflow;

import com.apiot.mediflow.referral.*;
import com.apiot.mediflow.test.MedicalTest;
import com.apiot.mediflow.test.MedicalTestDto;
import com.apiot.mediflow.test.MedicalTestRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ReferralServiceTests {

    private ReferralRepository referralRepository;
    private MedicalTestRepository medicalTestRepository;
    private ReferralService referralService;

    private Set<MedicalTest> medicalTests;

    @BeforeEach
    void setup() {
        referralRepository = mock(ReferralRepository.class);
        medicalTestRepository = mock(MedicalTestRepository.class);
        referralService = new ReferralService(referralRepository, medicalTestRepository);

        medicalTests = Set.of(
                new MedicalTest(1L, "TSH", "Badanie funkcji tarczycy", 45.99F),
                new MedicalTest(2L, "Glukoza", "Badanie poziomu glukozy na czczo", 19.99F));
    }

    @Test
    void shouldReturnReferrals() {
        // given
        List<Referral> referralList = List.of(
                new Referral(1L, "Jan Kowalski", "A25000001", LocalDateTime.now(), medicalTests),
                new Referral(2L, "Jakub Kozłowski", "A25000002", LocalDateTime.now(), medicalTests)
        );

        when(referralRepository.findAllWithMedicalTests()).thenReturn(referralList);

        // when
        List<ReferralDto> result = referralService.getAllReferrals();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Jan Kowalski", result.get(0).getReferrer());
        assertEquals("Jakub Kozłowski", result.get(1).getReferrer());
        assertEquals("A25000001", result.get(0).getReferral_number());
        assertEquals("A25000002", result.get(1).getReferral_number());
        assertEquals(2, result.get(0).getMedicalTestDtoSet().size());
        assertEquals(2, result.get(1).getMedicalTestDtoSet().size());

        verify(referralRepository, times(1)).findAllWithMedicalTests();
    }

    @Test
    void shouldReturnReferralById() {
        // given
        Referral referral = new Referral(1L, "Jan Kowalski", "A25000001", LocalDateTime.now(), medicalTests);

        when(referralRepository.findByIdWithMedicalTests(1L)).thenReturn(Optional.of(referral));

        // when
        ReferralDto result = referralService.getReferralById(1L);

        // then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Jan Kowalski", result.getReferrer());
        assertEquals("A25000001", result.getReferral_number());
        assertEquals(2, result.getMedicalTestDtoSet().size());

        verify(referralRepository, times(1)).findByIdWithMedicalTests(1L);
    }

    @Test
    void shouldThrowExceptionWhenReferralNotFound() {
        // given
        when(referralRepository.findByIdWithMedicalTests(123L)).thenReturn(Optional.empty());

        // when + then
        assertThrows(EntityNotFoundException.class, () -> referralService.getReferralById(123L));
    }

    @Test
    void shouldCreateReferral() {
        // given
        Set<Long> medicalTestIds = Set.of(1L, 2L);
        ReferralCreateDto referralDto = new ReferralCreateDto(null, "Jan Kowalski", medicalTestIds);

        Referral savedReferral = new Referral(1L, "Jan Kowalski", "A25000003",
                LocalDateTime.now(), medicalTests);

        when(referralRepository.save(any(Referral.class))).thenReturn(savedReferral);

        // when
        ReferralDto result = referralService.createReferral(referralDto);

        // then
        assertNotNull(result.getId());
        assertEquals(savedReferral.getReferrer(), result.getReferrer());
        assertEquals(savedReferral.getReferral_number(), result.getReferral_number());
        assertEquals(2, result.getMedicalTestDtoSet().size());
        verify(referralRepository, times(1)).save(any(Referral.class));
    }
}
