package com.apiot.mediflow.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleResponseDto {

    private Long id;
    private String sampleCode;
}
