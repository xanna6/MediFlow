package com.apiot.mediflow.test;

import org.springframework.stereotype.Service;

@Service
public class MedicalTestMapper {

    public static MedicalTestDto mapMedicalTestToMedicalTestDto(MedicalTest medicalTest) {
        MedicalTestDto medicalTestDto = new MedicalTestDto();
        medicalTestDto.setId(medicalTest.getId());
        medicalTestDto.setName(medicalTest.getName());
        return medicalTestDto;
    }

    public MedicalTest mapMedicalTestDtoToMedicalTest(MedicalTestDto medicalTestDto) {
        MedicalTest medicalTest = new MedicalTest();
        medicalTest.setId(medicalTestDto.getId());
        medicalTest.setName(medicalTestDto.getName());
        return medicalTest;
    }
}
