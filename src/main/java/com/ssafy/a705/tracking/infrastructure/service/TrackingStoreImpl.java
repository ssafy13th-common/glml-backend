package com.ssafy.a705.tracking.infrastructure.service;

import com.ssafy.a705.tracking.domain.entity.Tracking;
import com.ssafy.a705.tracking.domain.repository.TrackingRepository;
import com.ssafy.a705.tracking.domain.service.TrackingStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackingStoreImpl implements TrackingStore {

    private final TrackingRepository trackingRepository;

    @Override
    public Tracking save(Tracking tracking) {
        return trackingRepository.save(tracking);
    }
}
