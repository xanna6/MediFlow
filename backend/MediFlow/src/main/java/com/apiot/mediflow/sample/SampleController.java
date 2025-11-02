package com.apiot.mediflow.sample;

import org.springframework.http.HttpStatus;
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

    @PostMapping("/{id}/results")
    public ResponseEntity<SampleResponseDto> updateSampleResults(@PathVariable Long id, @RequestBody UpdateSampleResultsDto dto) {

        if (!dto.getSampleId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        SampleResponseDto updatedSample = sampleService.updateSampleResults(dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedSample);
    }

    @GetMapping
    public ResponseEntity<List<SampleResponseDto>> getAllSamples() {
        List<SampleResponseDto> sampleResponseDtos = sampleService.getAllSamples();
        return ResponseEntity.status(HttpStatus.OK).body(sampleResponseDtos);
    }
}
