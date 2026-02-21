package com.apiot.mediflow.sample;

import com.apiot.mediflow.appointment.Appointment;
import com.apiot.mediflow.users.LabEmployee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonIgnore
    @ToString.Exclude
    private Appointment appointment;

    private String sampleCode;
    private LocalDateTime collectionDate;

    @Enumerated(EnumType.STRING)
    private SampleStatus status = SampleStatus.CREATED;

    @OneToMany(mappedBy = "sample", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SampleTest> sampleTests = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "lab_employee_id")
    @JsonIgnore
    @ToString.Exclude
    private LabEmployee labEmployee;
}
