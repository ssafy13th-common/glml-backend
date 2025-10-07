package com.ssafy.a705.location.infrastructure.service;

import com.ssafy.a705.location.domain.entity.Location;
import com.ssafy.a705.location.domain.repository.LocationRepository;
import com.ssafy.a705.location.domain.service.LocationStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationStoreImpl implements LocationStore {

    private final LocationRepository locationRepository;

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }
}
