package com.apiot.mediflow.appointment;

import com.apiot.mediflow.collectionPoint.CollectionPoint;
import com.apiot.mediflow.referral.Referral;
import com.apiot.mediflow.users.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referral_id", nullable = false)
    private Referral referral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_point_id", nullable = false)
    private CollectionPoint collectionPoint;

}
