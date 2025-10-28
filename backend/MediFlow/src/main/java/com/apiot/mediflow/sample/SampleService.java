package com.apiot.mediflow.sample;

import com.apiot.mediflow.appointment.Appointment;
import com.apiot.mediflow.appointment.AppointmentRepository;
import com.apiot.mediflow.appointment.AppointmentStatus;
import com.apiot.mediflow.barcodeGenerator.SampleCodeGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SampleService {

    private final SampleRepository sampleRepository;
    private final AppointmentRepository appointmentRepository;
    private final SampleCodeGenerator sampleCodeGenerator;

    public SampleService(SampleRepository sampleRepository, AppointmentRepository appointmentRepository,
                         SampleCodeGenerator sampleCodeGenerator) {
        this.sampleRepository = sampleRepository;
        this.appointmentRepository = appointmentRepository;
        this.sampleCodeGenerator = sampleCodeGenerator;
    }

    @Transactional
    protected SampleResponseDto createSample(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        Sample sample = new Sample();
        sample.setAppointment(appointment);
        sample.setSampleCode(sampleCodeGenerator.generateSampleCode());
        sample.setCollectionDate(LocalDateTime.now());

        List<SampleTest> tests = appointment.getReferral().getMedicalTests().stream()
                .map(test -> {
                    SampleTest sampleTest = new SampleTest();
                    sampleTest.setSample(sample);
                    sampleTest.setMedicalTest(test);
                    sampleTest.setUnit(test.getUnit());
                    sampleTest.setStandard(test.getStandard());
                    return sampleTest;
                })
                .toList();

        sample.setSampleTests(tests);

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        sample.setAppointment(appointment);

        Sample savedSample = sampleRepository.save(sample);

        return mapSampleToSampleDto(savedSample);
    }

    protected SampleResponseDto getSampleById(Long id) {
        Sample sample = sampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sample not found"));
        return mapSampleToSampleDto(sample);
    }

    protected List<SampleResponseDto> getAllSamples() {
        List<Sample> samples = sampleRepository.findAll();
        return samples.stream().map(this::mapSampleToSampleDto).collect(Collectors.toList());
    }

    @Transactional
    protected SampleResponseDto updateSampleResults(UpdateSampleResultsDto dto) {
        Sample sample = sampleRepository.findById(dto.getSampleId())
                .orElseThrow(() -> new RuntimeException("Sample not found"));

        Map<Long, String> resultsMap = dto.getTests().stream()
                .collect(Collectors.toMap(UpdateSampleTestResultDto::getSampleTestId,
                        UpdateSampleTestResultDto::getResult));

        for (SampleTest st : sample.getSampleTests()) {
            if (resultsMap.containsKey(st.getId())) {
                st.setResult(resultsMap.get(st.getId()));
                st.setResultDate(LocalDateTime.now());
            }
        }

        Sample updatedSample = sampleRepository.save(sample);

        return mapSampleToSampleDto(updatedSample);
    }

    private SampleResponseDto mapSampleToSampleDto(Sample sample) {
        SampleResponseDto sampleResponseDto = new SampleResponseDto();
        sampleResponseDto.setId(sample.getId());
        sampleResponseDto.setSampleCode(sample.getSampleCode());
        sampleResponseDto.setCreatedAt(sample.getCollectionDate());
        sampleResponseDto.setSampleTestDtos(sample
                .getSampleTests()
                .stream()
                .map(this::mapSampleTestToSampleTestDto)
                .collect(Collectors.toList())
        );
        return sampleResponseDto;
    }

    private SampleTestDto mapSampleTestToSampleTestDto(SampleTest sampleTest) {
        SampleTestDto sampleTestDto = new SampleTestDto();
        sampleTestDto.setId(sampleTest.getId());
        sampleTestDto.setName(sampleTest.getMedicalTest().getName());
        sampleTestDto.setResult(sampleTest.getResult());
        sampleTestDto.setUnit(sampleTest.getUnit());
        sampleTestDto.setStandard(sampleTest.getStandard());
        sampleTestDto.setResultDate(sampleTest.getResultDate());
        return sampleTestDto;
    }
}
