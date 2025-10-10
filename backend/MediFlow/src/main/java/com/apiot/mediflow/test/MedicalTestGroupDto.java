package com.apiot.mediflow.test;

import lombok.Data;

import java.util.List;

@Data
public class MedicalTestGroupDto {

    private String category;
    private List<MedicalTestDto> medicalTests;

    public MedicalTestGroupDto(String category, List<MedicalTestDto> medicalTests) {
        this.category = category;
        this.medicalTests = medicalTests;
    }
}
