package com.apiot.mediflow.test;

import com.apiot.mediflow.referral.Referral;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class MedicalTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private float cost;
    @ManyToMany(mappedBy = "medicalTests")
    @JsonIgnore
    private Set<Referral> referrals;

    public MedicalTest(Long id, String name, String description, float cost) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
    }

    public MedicalTest() {
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
