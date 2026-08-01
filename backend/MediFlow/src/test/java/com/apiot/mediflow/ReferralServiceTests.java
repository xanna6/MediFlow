package com.apiot.mediflow;

import com.apiot.mediflow.auth.User;
import com.apiot.mediflow.auth.UserRepository;
import com.apiot.mediflow.referral.*;
import com.apiot.mediflow.referralNumberGenerator.ReferralNumberGenerator;
import com.apiot.mediflow.test.MedicalTest;
import com.apiot.mediflow.test.MedicalTestRepository;
import com.apiot.mediflow.users.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
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
    private UserRepository userRepository;
    private PatientRepository patientRepository;
    private ReferralService referralService;
    private ReferralNumberGenerator referralNumberGenerator;

    private Set<MedicalTest> medicalTests;
    private Doctor doctor;

    @BeforeEach
    void setup() {
        referralRepository = mock(ReferralRepository.class);
        medicalTestRepository = mock(MedicalTestRepository.class);
        userRepository = mock(UserRepository.class);
        patientRepository = mock(PatientRepository.class);
        referralNumberGenerator = mock(ReferralNumberGenerator.class);
        referralService = new ReferralService(referralRepository, medicalTestRepository, userRepository, patientRepository,
                referralNumberGenerator);

        medicalTests = Set.of(
                new MedicalTest(1L, "TSH", "Badanie funkcji tarczycy", "mIU/l", "0.4 - 4.0"),
                new MedicalTest(2L, "Glukoza", "Badanie poziomu glukozy na czczo", "mg/dl", "70 - 99"));

        doctor = new Doctor(1L, "Jan", "Kowalski", "Kardiolog");
    }

    @Test
    void shouldReturnReferrals() {
        // given
        List<Referral> referralList = List.of(
                new Referral(1L, "A25000001", LocalDateTime.now(), doctor, medicalTests),
                new Referral(2L, "A25000002", LocalDateTime.now(), doctor, medicalTests)
        );

        when(referralRepository.findAllWithMedicalTests()).thenReturn(referralList);

        // when
        List<ReferralDto> result = referralService.getAllReferrals();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Jan Kowalski", result.get(0).getReferrerFirstname().concat(" ").concat(result.get(0).getReferrerLastname()));
        assertEquals("Jan Kowalski", result.get(1).getReferrerFirstname().concat(" ").concat(result.get(1).getReferrerLastname()));
        assertEquals("A25000001", result.get(0).getReferralNumber());
        assertEquals("A25000002", result.get(1).getReferralNumber());
        assertEquals(2, result.get(0).getMedicalTestDtoSet().size());
        assertEquals(2, result.get(1).getMedicalTestDtoSet().size());

        verify(referralRepository, times(1)).findAllWithMedicalTests();
    }

    @Test
    void shouldReturnReferralById() {
        // given
        Referral referral = new Referral(1L, "A25000001", LocalDateTime.now(), doctor, medicalTests);

        when(referralRepository.findByIdWithMedicalTests(1L)).thenReturn(Optional.of(referral));

        // when
        ReferralDto result = referralService.getReferralById(1L);

        // then
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Jan Kowalski", result.getReferrerFirstname().concat(" ").concat(result.getReferrerLastname()));
        assertEquals("A25000001", result.getReferralNumber());
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
    void shouldCreateReferralAndSavePatientIfNotExists() {
        // given
        String username = "doctor1";

        // mock security context
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        ReferralCreateDto dto = new ReferralCreateDto(new PatientDto("Jan", "Kowalski", "12345678901",
                LocalDate.of(1990, 1, 1)), Set.of(1L));

        MedicalTest test = new MedicalTest();
        test.setId(1L);

        when(medicalTestRepository.findAllById(any()))
                .thenReturn(List.of(test));

        when(referralNumberGenerator.generateNextNumber())
                .thenReturn("REF123");

        User user = new User();
        Doctor doctor = new Doctor();
        user.setDoctor(doctor);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(patientRepository.findByPesel("12345678901"))
                .thenReturn(Optional.empty());

        Patient savedPatient = new Patient();
        savedPatient.setPesel("12345678901");

        when(patientRepository.save(any(Patient.class)))
                .thenReturn(savedPatient);

        Referral savedReferral = new Referral("REF123", doctor, savedPatient, Set.of(test));

        when(referralRepository.save(any(Referral.class)))
                .thenReturn(savedReferral);

        // when
        ReferralDto result = referralService.createReferral(dto);

        // then
        assertNotNull(result);

        verify(referralRepository).save(any(Referral.class));
        verify(patientRepository).save(any(Patient.class));
        verify(userRepository).findByUsername(username);
    }
}
