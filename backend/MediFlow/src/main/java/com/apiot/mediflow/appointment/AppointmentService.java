package com.apiot.mediflow.appointment;

import com.apiot.mediflow.collectionPoint.CollectionPoint;
import com.apiot.mediflow.collectionPoint.CollectionPointRepository;
import com.apiot.mediflow.referral.Referral;
import com.apiot.mediflow.referral.ReferralRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ReferralRepository referralRepository;
    private final CollectionPointRepository collectionPointRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, ReferralRepository referralRepository,
                              CollectionPointRepository collectionPointRepository) {
        this.appointmentRepository = appointmentRepository;
        this.referralRepository = referralRepository;
        this.collectionPointRepository = collectionPointRepository;
    }

    @Transactional
    protected AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto) {
        Referral referral = referralRepository.findById(appointmentRequestDto.getReferralId())
                .orElseThrow(() -> new RuntimeException("Referral does not exist"));
        CollectionPoint collectionPoint = collectionPointRepository.findById(appointmentRequestDto.getCollectionPointId())
                .orElseThrow(() -> new RuntimeException("Collection point does not exist"));

        if (appointmentRepository.existsByReferral(referral)) {
            throw new RuntimeException("Referral has already been used");
        }

        if (appointmentRepository.existsByCollectionPointAndDate(collectionPoint, appointmentRequestDto.getDate())) {
            throw new RuntimeException("This time slot is already taken at the collection point");
        }

        Appointment appointment = mapAppointmentRequestDtoToAppointment(appointmentRequestDto, referral, collectionPoint);

        Appointment savedAppointment = appointmentRepository.save(appointment);

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

    private AppointmentResponseDto mapAppointmentToAppointmentResponseDto(Appointment appointment) {
        AppointmentResponseDto appointmentResponseDto = new AppointmentResponseDto();
        appointmentResponseDto.setId(appointment.getId());
        appointmentResponseDto.setDate(appointment.getDate());
        appointmentResponseDto.setReferralId(appointment.getReferral().getId());
        appointmentResponseDto.setCollectionPointId(appointment.getCollectionPoint().getId());
        return appointmentResponseDto;
    }

    private Appointment mapAppointmentRequestDtoToAppointment(AppointmentRequestDto appointmentRequestDto,
                                                              Referral referral, CollectionPoint collectionPoint) {
        Appointment appointment = new Appointment();
        appointment.setDate(appointmentRequestDto.getDate());
        appointment.setReferral(referral);
        appointment.setCollectionPoint(collectionPoint);
        return appointment;
    }

}
