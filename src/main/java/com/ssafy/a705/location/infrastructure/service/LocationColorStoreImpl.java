package com.ssafy.a705.location.infrastructure.service;

import com.ssafy.a705.location.domain.entity.LocationColor;
import com.ssafy.a705.location.domain.repository.LocationColorRepository;
import com.ssafy.a705.location.domain.service.LocationColorStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationColorStoreImpl implements LocationColorStore {

    private final LocationColorRepository colorRepository;

    @Override
    public LocationColor save(LocationColor color) {
        return colorRepository.save(color);
    }
}
