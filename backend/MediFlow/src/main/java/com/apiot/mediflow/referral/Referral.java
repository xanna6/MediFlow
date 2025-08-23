package com.apiot.mediflow.referral;

import com.apiot.mediflow.appointment.Appointment;
import com.apiot.mediflow.test.MedicalTest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
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

    private String referrer;
    private String referral_number;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "referral_test",
            joinColumns = @JoinColumn(name = "referral_id"),
            inverseJoinColumns = @JoinColumn(name = "test_id"))
    private Set<MedicalTest> medicalTests;

    @OneToMany(mappedBy = "referral")
    private List<Appointment> appointmentList;

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
