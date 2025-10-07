package com.ssafy.a705.location.infrastructure.service;

import com.ssafy.a705.location.domain.entity.Location;
import com.ssafy.a705.location.domain.exception.LocationNotFoundException;
import com.ssafy.a705.location.domain.repository.LocationRepository;
import com.ssafy.a705.location.domain.service.LocationReader;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationReaderImpl implements LocationReader {

    private final LocationRepository locationRepository;

    @Override
    public Optional<Location> findByCode(Integer code) {
        return locationRepository.findByCode(code);
    }

    @Override
    public Location getByCode(Integer code) {
        Optional<Location> location = locationRepository.findByCode(code);
        if (location.isEmpty()) {
            throw new LocationNotFoundException();
        }
        return location.get();
    }
}
