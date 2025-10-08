package com.ssafy.a705.tracking.infrastructure.service;

import com.ssafy.a705.tracking.domain.entity.Tracking;
import com.ssafy.a705.tracking.domain.exception.TrackingNotFoundException;
import com.ssafy.a705.tracking.domain.repository.TrackingRepository;
import com.ssafy.a705.tracking.domain.service.TrackingReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackingReaderImpl implements TrackingReader {

    private final TrackingRepository trackingRepository;

    @Override
    public Tracking getTracking(String trackingId) {
        return trackingRepository.findByIdAndNotDeleted(trackingId)
                .orElseThrow(TrackingNotFoundException::new);
    }
}
