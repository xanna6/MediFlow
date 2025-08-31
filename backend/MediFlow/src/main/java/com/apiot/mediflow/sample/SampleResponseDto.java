package com.apiot.mediflow.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleResponseDto {

    private Long id;
    private String sampleCode;
    private LocalDateTime createdAt;
    private List<SampleTestDto> sampleTestDtos;
}
