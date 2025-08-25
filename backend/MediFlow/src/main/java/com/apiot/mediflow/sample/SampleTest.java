package com.apiot.mediflow.sample;

import com.apiot.mediflow.test.MedicalTest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Sample sample;

    @ManyToOne
    @JoinColumn(name = "medical_test_id")
    private MedicalTest medicalTest;

    private String result;
    private String unit;
    private String standard;
    private LocalDateTime resultDate;
}
