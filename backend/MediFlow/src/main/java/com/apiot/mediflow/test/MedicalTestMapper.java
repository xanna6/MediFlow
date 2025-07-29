package com.apiot.mediflow.test;

import org.springframework.stereotype.Service;

@Service
public class MedicalTestMapper {

    public static MedicalTestDto mapMedicalTestToMedicalTestDto(MedicalTest medicalTest) {
        MedicalTestDto medicalTestDto = new MedicalTestDto();
        medicalTestDto.setId(medicalTest.getId());
        medicalTestDto.setName(medicalTest.getName());
        medicalTestDto.setDescription(medicalTest.getDescription());
        medicalTestDto.setCost(medicalTest.getCost());
        return medicalTestDto;
    }

    public MedicalTest mapMedicalTestDtoToMedicalTest(MedicalTestDto medicalTestDto) {
        MedicalTest medicalTest = new MedicalTest();
        medicalTest.setId(medicalTestDto.getId());
        medicalTest.setName(medicalTestDto.getName());
        medicalTest.setDescription(medicalTestDto.getDescription());
        medicalTest.setCost(medicalTestDto.getCost());
        return medicalTest;
    }
}
