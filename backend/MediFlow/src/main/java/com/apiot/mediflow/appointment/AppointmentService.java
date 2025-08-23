package com.apiot.mediflow.appointment;

import com.apiot.mediflow.collectionPoint.CollectionPoint;
import com.apiot.mediflow.collectionPoint.CollectionPointRepository;
import com.apiot.mediflow.referral.Referral;
import com.apiot.mediflow.referral.ReferralRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Appointment appointment = new Appointment();
        appointment.setDate(appointmentRequestDto.getDate());
        appointment.setReferral(referral);
        appointment.setCollectionPoint(collectionPoint);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return new AppointmentResponseDto(savedAppointment);
    }

}
