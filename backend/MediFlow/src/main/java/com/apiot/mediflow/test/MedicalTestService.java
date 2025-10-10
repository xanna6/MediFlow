package com.apiot.mediflow.test;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MedicalTestService {

    private final MedicalTestRepository medicalTestRepository;

    public MedicalTestService(MedicalTestRepository medicalTestRepository) {
        this.medicalTestRepository = medicalTestRepository;
    }

    public List<MedicalTestGroupDto> getGroupedMedicalTests() {
        List<MedicalTest> allTests = medicalTestRepository.findAllWithCategory();

        Map<String, List<MedicalTestDto>> grouped = allTests.stream()
                .collect(Collectors.groupingBy(
                        test -> test.getCategory() != null ? test.getCategory().getName() : "Inne",
                        Collectors.mapping(
                                test -> new MedicalTestDto(
                                        test.getId(),
                                        test.getName()
                                ),
                                Collectors.toList()
                        )
                ));

         return grouped.entrySet().stream()
                .map(e -> new MedicalTestGroupDto(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }
}
