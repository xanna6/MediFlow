package com.apiot.mediflow.test;

import com.apiot.mediflow.referral.Referral;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalTestDto {
    private Long id;
    private String name;
    private String description;
    private float cost;
}
