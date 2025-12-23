package com.apiot.mediflow.collectionPoint;

import com.apiot.mediflow.appointment.Appointment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CollectionPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String street;
    private String city;
    private String postalCode;
    private LocalTime openedFrom;
    private LocalTime openedTo;
    private float latitude;
    private float longitude;

    @OneToMany(mappedBy = "collectionPoint")
    @JsonIgnore
    @ToString.Exclude
    private List<Appointment> appointmentList;
}
