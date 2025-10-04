package com.apiot.mediflow;

import com.apiot.mediflow.referral.*;
import com.apiot.mediflow.test.MedicalTest;
import com.apiot.mediflow.test.MedicalTestRepository;
import com.apiot.mediflow.users.Doctor;
import com.apiot.mediflow.users.DoctorRepository;
import com.apiot.mediflow.users.Patient;
import com.apiot.mediflow.users.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private ReferralService referralService;

    private Set<MedicalTest> medicalTests;
    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setup() {
        referralRepository = mock(ReferralRepository.class);
        medicalTestRepository = mock(MedicalTestRepository.class);
        doctorRepository = mock(DoctorRepository.class);
        patientRepository = mock(PatientRepository.class);
        referralService = new ReferralService(referralRepository, medicalTestRepository, doctorRepository, patientRepository);

        medicalTests = Set.of(
                new MedicalTest(1L, "TSH", "Badanie funkcji tarczycy", 45.99F, "mIU/l", "0.4 - 4.0"),
                new MedicalTest(2L, "Glukoza", "Badanie poziomu glukozy na czczo", 19.99F, "mg/dl", "70 - 99"));

        doctor = new Doctor(1L, "Jan", "Kowalski", "Kardiolog");

        patient = new Patient(1L, "Julia", "Kot", "85031333842",
                LocalDate.of(1985,3,13), 123456789);
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
    void shouldCreateReferral() {
        // given
        Set<Long> medicalTestIds = Set.of(1L, 2L);
        ReferralCreateDto referralDto = new ReferralCreateDto(1L, 1L,"A25000003", medicalTestIds);

        Referral savedReferral = new Referral(1L, "A25000003", LocalDateTime.now(), doctor, medicalTests);

        when(referralRepository.save(any(Referral.class))).thenReturn(savedReferral);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // when
        ReferralDto result = referralService.createReferral(referralDto);

        // then
        assertNotNull(result.getId());
        assertEquals(savedReferral.getDoctor().getLastname(), result.getReferrerLastname());
        assertEquals(savedReferral.getReferralNumber(), result.getReferralNumber());
        assertEquals(2, result.getMedicalTestDtoSet().size());
        verify(referralRepository, times(1)).save(any(Referral.class));
    }
}
