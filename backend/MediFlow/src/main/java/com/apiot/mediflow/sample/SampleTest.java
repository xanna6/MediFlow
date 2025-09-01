package com.apiot.mediflow.sample;

import com.apiot.mediflow.test.MedicalTest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sample_id")
    @JsonIgnore
    @ToString.Exclude
    private Sample sample;

    @ManyToOne
    @JoinColumn(name = "medical_test_id")
    @JsonIgnore
    @ToString.Exclude
    private MedicalTest medicalTest;

    private String result;
    private String unit;
    private String standard;
    private LocalDateTime resultDate;
}
