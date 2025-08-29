package com.apiot.mediflow.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleTestDto {

    private Long id;
    private String name;
    private String result;
    private String unit;
    private String standard;
    private LocalDateTime resultDate;
}
