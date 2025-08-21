package com.apiot.mediflow.collectionPoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/collection-points")
@RestController
public class CollectionPointController {

    private final CollectionPointService collectionPointService;

    public CollectionPointController(CollectionPointService collectionPointService) {
        this.collectionPointService = collectionPointService;
    }

    @GetMapping
    public ResponseEntity<List<CollectionPointDto>> getAllCollectionPoints() {
        List<CollectionPointDto> collectionPointDtos = collectionPointService.getAllCollectionPoints();
        return ResponseEntity.ok(collectionPointDtos);
    }
}
