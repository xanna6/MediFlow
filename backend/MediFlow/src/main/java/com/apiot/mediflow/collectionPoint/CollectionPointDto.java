package com.apiot.mediflow.collectionPoint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionPointDto {

    private Long id;
    private String name;
    private String street;
    private String city;
    private String postalCode;
    private LocalTime openedFrom;
    private LocalTime openedTo;
}
