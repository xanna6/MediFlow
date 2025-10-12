package com.apiot.mediflow.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {

    private String firstName;
    private String lastName;
    private String pesel;
    private LocalDate birthDate;
}
