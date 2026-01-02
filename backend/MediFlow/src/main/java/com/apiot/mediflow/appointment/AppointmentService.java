package com.apiot.mediflow.appointment;

import com.apiot.mediflow.collectionPoint.CollectionPoint;
import com.apiot.mediflow.collectionPoint.CollectionPointRepository;
import com.apiot.mediflow.mail.EmailService;
import com.apiot.mediflow.referral.Referral;
import com.apiot.mediflow.referral.ReferralRepository;
import com.apiot.mediflow.users.Patient;
import com.apiot.mediflow.users.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ReferralRepository referralRepository;
    private final CollectionPointRepository collectionPointRepository;
    private final PatientRepository patientRepository;
    private final EmailService emailService;

    public AppointmentService(AppointmentRepository appointmentRepository, ReferralRepository referralRepository,
                              CollectionPointRepository collectionPointRepository, PatientRepository patientRepository,
                              EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.referralRepository = referralRepository;
        this.collectionPointRepository = collectionPointRepository;
        this.patientRepository = patientRepository;
        this.emailService = emailService;
    }

    @Transactional
    protected AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto) {
        Patient patient = patientRepository.findByPesel(appointmentRequestDto.getPesel())
                .orElseThrow(() -> new EntityNotFoundException("Patient not found"));

        Referral referral = referralRepository.findByReferralNumber(appointmentRequestDto.getReferralNumber())
                .orElseThrow(() -> new EntityNotFoundException("Referral does not exist"));

        if (!referral.getPatient().equals(patient)) {
            throw new IllegalArgumentException("Referral does not belong to the patient");
        }

        CollectionPoint collectionPoint = collectionPointRepository.findById(appointmentRequestDto.getCollectionPointId())
                .orElseThrow(() -> new EntityNotFoundException("Collection point does not exist"));

        if (appointmentRepository.existsByReferral(referral)) {
            throw new RuntimeException("Referral has already been used");
        }

        if (appointmentRepository.existsByCollectionPointAndDate(collectionPoint, appointmentRequestDto.getDate())) {
            throw new RuntimeException("This time slot is already taken at the collection point");
        }

        Appointment appointment = mapAppointmentRequestDtoToAppointment(appointmentRequestDto, referral, collectionPoint);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        if (patient.getEmail() != null && !patient.getEmail().isBlank()) {
            emailService.sendAppointmentConfirmationMail(
                    patient.getEmail(),
                    patient.getFirstName(),
                    savedAppointment.getDate(),
                    savedAppointment.getCollectionPoint().getName()
            );
        }

        return mapAppointmentToAppointmentResponseDto(savedAppointment);
    }

    protected List<AppointmentResponseDto> getAppointments() {
        //TODO: return a list of appointments depending on the user's role
        return appointmentRepository.findAll()
                .stream()
                .map(this::mapAppointmentToAppointmentResponseDto)
                .collect(Collectors.toList());
    }

    protected List<AppointmentResponseDto> getAppointmentsForDay(LocalDate day) {
        LocalDateTime startOfDay = day.atStartOfDay();
        LocalDateTime endOfDay = day.plusDays(1).atStartOfDay();

        return appointmentRepository.findAllByDateBetween(startOfDay, endOfDay)
                .stream()
                .map(this::mapAppointmentToAppointmentResponseDto)
                .collect(Collectors.toList());
    }

    public AvailableSlotsResponse getAvailableSlots(Long pointId, LocalDate date) {
        CollectionPoint point = collectionPointRepository.findById(pointId)
                .orElseThrow(() -> new RuntimeException("Collection point not found"));

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<Appointment> takenAppointments = appointmentRepository.findByCollectionPointIdAndDateBetween(pointId, startOfDay, endOfDay);
        Set<LocalTime> takenTimes = takenAppointments.stream()
                .map(appointment -> appointment.getDate().toLocalTime())
                .collect(Collectors.toSet());

        List<String> available = new ArrayList<>();
        LocalTime time = point.getOpenedFrom();
        while (time.isBefore(point.getOpenedTo())) {
            if (!takenTimes.contains(time)) {
                available.add(time.toString());
            }
            time = time.plusMinutes(5);
        }

        return new AvailableSlotsResponse(pointId, date.toString(), available);
    }

    private AppointmentResponseDto mapAppointmentToAppointmentResponseDto(Appointment appointment) {
        AppointmentResponseDto appointmentResponseDto = new AppointmentResponseDto();
        appointmentResponseDto.setId(appointment.getId());
        appointmentResponseDto.setDate(appointment.getDate());
        appointmentResponseDto.setStatus(appointment.getStatus().toString());
        appointmentResponseDto.setReferralNumber(appointment.getReferral().getReferralNumber());
        appointmentResponseDto.setCollectionPointId(appointment.getCollectionPoint().getId());
        appointmentResponseDto.setPatientName(appointment.getReferral().getPatient().getFirstName().concat(" ")
                .concat(appointment.getReferral().getPatient().getLastName()));
        appointmentResponseDto.setPesel(appointment.getReferral().getPatient().getPesel());
        return appointmentResponseDto;
    }

    private Appointment mapAppointmentRequestDtoToAppointment(AppointmentRequestDto appointmentRequestDto,
                                                              Referral referral, CollectionPoint collectionPoint) {
        Appointment appointment = new Appointment();
        appointment.setDate(appointmentRequestDto.getDate());
        appointment.setReferral(referral);
        appointment.setCollectionPoint(collectionPoint);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        return appointment;
    }

}
