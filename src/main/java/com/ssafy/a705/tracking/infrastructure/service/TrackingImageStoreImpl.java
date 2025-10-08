package com.ssafy.a705.tracking.infrastructure.service;

import com.ssafy.a705.tracking.domain.entity.TrackingImage;
import com.ssafy.a705.tracking.domain.repository.TrackingImageRepository;
import com.ssafy.a705.tracking.domain.service.TrackingImageStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackingImageStoreImpl implements TrackingImageStore {

    private final TrackingImageRepository imageRepository;

    @Override
    public TrackingImage save(TrackingImage image) {
        return imageRepository.save(image);
    }
}
