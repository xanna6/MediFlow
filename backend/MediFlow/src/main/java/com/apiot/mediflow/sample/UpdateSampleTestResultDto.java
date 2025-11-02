package com.apiot.mediflow.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSampleTestResultDto {

    private Long id;
    private String result;
}
