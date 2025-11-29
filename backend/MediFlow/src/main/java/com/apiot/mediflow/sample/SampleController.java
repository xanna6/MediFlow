package com.apiot.mediflow.sample;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/samples")
public class SampleController {

    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @PostMapping
    public ResponseEntity<SampleResponseDto> createSample(@RequestBody SampleRequestDto sampleRequestDto) {
        SampleResponseDto sampleResponseDto = sampleService.createSample(sampleRequestDto.getAppointmentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(sampleResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SampleResponseDto> getSampleById(@PathVariable long id) {
        SampleResponseDto sampleResponseDto = sampleService.getSampleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(sampleResponseDto);
    }

    @GetMapping("/by-code")
    public ResponseEntity<SampleResponseDto> getSampleByPeselAndSampleCode(@RequestParam String pesel, @RequestParam String sampleCode) {
        try {
            SampleResponseDto response = sampleService.getSampleByPeselAndSampleCode(pesel, sampleCode);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping("/{id}/results")
    public ResponseEntity<SampleResponseDto> updateSampleResults(@PathVariable Long id, @RequestBody UpdateSampleResultsDto dto) {

        if (!dto.getSampleId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        SampleResponseDto updatedSample = sampleService.updateSampleResults(dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedSample);
    }

    @GetMapping("/{id}/results/pdf")
    public ResponseEntity<byte[]> getSampleResultsPdf(@PathVariable Long id) {
        byte[] pdfData = sampleService.generateSampleResultsPdf(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData(
                "attachment",
                "wyniki-probki-" + id + ".pdf"
        );

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdfData);
    }

    @GetMapping
    public ResponseEntity<List<SampleResponseDto>> getAllSamples() {
        List<SampleResponseDto> sampleResponseDtos = sampleService.getAllSamples();
        return ResponseEntity.status(HttpStatus.OK).body(sampleResponseDtos);
    }
}
