package com.apiot.mediflow.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSampleResultsDto {

    private Long sampleId;
    private List<UpdateSampleTestResultDto> tests;
}
