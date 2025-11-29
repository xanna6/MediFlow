package com.apiot.mediflow.sample;

import com.apiot.mediflow.appointment.Appointment;
import com.apiot.mediflow.appointment.AppointmentRepository;
import com.apiot.mediflow.appointment.AppointmentStatus;
import com.apiot.mediflow.barcodeGenerator.SampleCodeGenerator;
import com.apiot.mediflow.users.Patient;
import jakarta.transaction.Transactional;
import org.openpdf.text.*;
import org.openpdf.text.Font;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public SampleResponseDto getSampleByPeselAndSampleCode(String pesel, String sampleCode) {
        Sample sample = sampleRepository.findByPatientPeselAndSampleCode(pesel, sampleCode)
                .orElseThrow(() -> new RuntimeException("Sample not found for provided data"));

        if (sample.getStatus() != SampleStatus.COMPLETED) {
            throw new IllegalStateException("Results for this sample are not yet available");
        }
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
                .collect(Collectors.toMap(UpdateSampleTestResultDto::getId,
                        UpdateSampleTestResultDto::getResult));

        for (SampleTest st : sample.getSampleTests()) {
            if (resultsMap.containsKey(st.getId())) {
                st.setResult(resultsMap.get(st.getId()));
                st.setResultDate(LocalDateTime.now());
            }
        }

        updateSampleStatus(sample);
        Sample updatedSample = sampleRepository.save(sample);

        return mapSampleToSampleDto(updatedSample);
    }

    private void updateSampleStatus(Sample sample) {
        List<SampleTest> sampleTests = sample.getSampleTests();

        if (sampleTests.isEmpty()) {
            sample.setStatus(SampleStatus.CREATED);
        } else if (sampleTests.stream().allMatch(r -> r.getResult() != null && !r.getResult().isEmpty())) {
            sample.setStatus(SampleStatus.COMPLETED);
        } else {
            sample.setStatus(SampleStatus.IN_PROGRESS);
        }
    }

    public byte[] generateSampleResultsPdf(Long sampleId) {
        Sample sample = sampleRepository.findById(sampleId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono próbki."));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.BLUE);
        Font subtitleFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.DARK_GRAY);
        Font tableHeaderFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE);

        Paragraph title = new Paragraph(sample.getAppointment().getCollectionPoint().getName(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph subtitle = new Paragraph("Raport wyników badań laboratoryjnych", subtitleFont);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        document.add(subtitle);
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Numer próbki: " + sample.getSampleCode()));
        document.add(new Paragraph("Data utworzenia: " + sample.getCollectionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        document.add(new Paragraph("Status: " + sample.getStatus()));
        document.add(new Paragraph(" "));

        Patient patient = sample.getAppointment().getReferral().getPatient();

        document.add(new Paragraph("Imię: " + patient.getFirstName()));
        document.add(new Paragraph("Nazwisko: " + patient.getLastName()));
        document.add(new Paragraph("Pesel: " + patient.getPesel()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        Stream.of("Badanie", "Wynik", "Jednostka", "Norma")
                .forEach(col -> {
                    PdfPCell cell = new PdfPCell(new Phrase(col, tableHeaderFont));
                    cell.setBackgroundColor(new Color(0, 102, 204));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                });

        sample.getSampleTests().forEach(test -> {
            table.addCell(test.getMedicalTest().getName());
            table.addCell(test.getResult() != null ? test.getResult() : "-");
            table.addCell(test.getUnit());
            table.addCell(test.getStandard());
        });

        document.add(table);

        document.add(new Paragraph("\nPodpis: ........................................"));
        document.add(new Paragraph("Data wydruku: " + LocalDate.now()));

        document.close();
        return out.toByteArray();
    }

    private SampleResponseDto mapSampleToSampleDto(Sample sample) {
        SampleResponseDto sampleResponseDto = new SampleResponseDto();
        sampleResponseDto.setId(sample.getId());
        sampleResponseDto.setSampleCode(sample.getSampleCode());
        sampleResponseDto.setCreatedAt(sample.getCollectionDate());
        sampleResponseDto.setStatus(sample.getStatus().toString());
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
