package com.apiot.mediflow.sample;

import com.apiot.mediflow.appointment.Appointment;
import com.apiot.mediflow.appointment.AppointmentRepository;
import com.apiot.mediflow.barcodeGenerator.SampleCodeGenerator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
                    return sampleTest;
                })
                .toList();

        sample.setTests(tests);

        Sample savedSample = sampleRepository.save(sample);

        return mapSampleToSampleDto(savedSample);
    }

    private SampleResponseDto mapSampleToSampleDto(Sample sample) {
        SampleResponseDto sampleResponseDto = new SampleResponseDto();
        sampleResponseDto.setId(sample.getId());
        sampleResponseDto.setSampleCode(sample.getSampleCode());
        return sampleResponseDto;
    }
}
