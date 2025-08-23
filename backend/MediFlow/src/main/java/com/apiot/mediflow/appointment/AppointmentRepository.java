package com.apiot.mediflow.appointment;

import com.apiot.mediflow.collectionPoint.CollectionPoint;
import com.apiot.mediflow.referral.Referral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByReferral(Referral referral);
    boolean existsByCollectionPointAndDate(CollectionPoint collectionPoint, LocalDateTime date);
    List<Appointment> findAllByDateBetween(LocalDateTime start, LocalDateTime end);
}
