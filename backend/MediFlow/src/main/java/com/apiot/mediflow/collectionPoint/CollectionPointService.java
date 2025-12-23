package com.apiot.mediflow.collectionPoint;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectionPointService {

    private final CollectionPointRepository collectionPointRepository;

    public CollectionPointService(CollectionPointRepository collectionPointRepository) {
        this.collectionPointRepository = collectionPointRepository;
    }

    protected List<CollectionPointDto> getAllCollectionPoints() {
        List<CollectionPoint> collectionPoints = collectionPointRepository.findAll();
        return collectionPoints.stream()
                .map(this::mapCollectionPointToDto)
                .collect(Collectors.toList());
    }

    private CollectionPointDto mapCollectionPointToDto(CollectionPoint collectionPoint) {
        CollectionPointDto collectionPointDto = new CollectionPointDto();
        collectionPointDto.setId(collectionPoint.getId());
        collectionPointDto.setName(collectionPoint.getName());
        collectionPointDto.setStreet(collectionPoint.getStreet());
        collectionPointDto.setCity(collectionPoint.getCity());
        collectionPointDto.setPostalCode(collectionPoint.getPostalCode());
        collectionPointDto.setOpenedFrom(collectionPoint.getOpenedFrom());
        collectionPointDto.setOpenedTo(collectionPoint.getOpenedTo());
        collectionPointDto.setLatitude(collectionPoint.getLatitude());
        collectionPointDto.setLongitude(collectionPoint.getLongitude());
        return collectionPointDto;
    }
}
