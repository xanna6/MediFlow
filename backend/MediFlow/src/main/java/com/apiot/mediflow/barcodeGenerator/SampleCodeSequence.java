package com.apiot.mediflow.barcodeGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleCodeSequence {

    @Id
    private String date;
    private int counter;
}
