package com.apiot.mediflow.test;

import com.apiot.mediflow.referral.Referral;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MedicalTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private float cost;
    private String unit;
    private String standard;

    @ManyToOne
    private MedicalTestCategory category;

    @ManyToMany(mappedBy = "medicalTests")
    @JsonIgnore
    @ToString.Exclude
    private Set<Referral> referrals;

    public MedicalTest(Long id, String name, String description, float cost, String unit, String standard) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.unit = unit;
        this.standard = standard;
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
