package com.apiot.mediflow.referral;

import com.apiot.mediflow.appointment.Appointment;
import com.apiot.mediflow.test.MedicalTest;
import com.apiot.mediflow.users.Doctor;
import com.apiot.mediflow.users.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String referralNumber;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "referral_test",
            joinColumns = @JoinColumn(name = "referral_id"),
            inverseJoinColumns = @JoinColumn(name = "test_id"))
    private Set<MedicalTest> medicalTests;

    @OneToOne(mappedBy = "referral")
    private Appointment appointment;

    public Referral(Long id, String referralNumber, LocalDateTime creationDate, Doctor doctor, Set<MedicalTest> medicalTests) {
        this.id = id;
        this.doctor = doctor;
        this.referralNumber = referralNumber;
        this.creationDate = creationDate;
        this.medicalTests = medicalTests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicalTest)) return false;
        MedicalTest that = (MedicalTest) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
