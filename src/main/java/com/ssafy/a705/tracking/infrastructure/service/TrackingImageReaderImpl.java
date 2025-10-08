package com.ssafy.a705.tracking.infrastructure.service;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.tracking.domain.entity.TrackingImage;
import com.ssafy.a705.tracking.domain.repository.TrackingImageRepository;
import com.ssafy.a705.tracking.domain.service.TrackingImageReader;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackingImageReaderImpl implements TrackingImageReader {

    private final TrackingImageRepository imageRepository;

    @Override
    public Optional<TrackingImage> getTrackingImage(Member member, String trackingId) {
        return imageRepository.findByMemberAndTrackingId(member, trackingId);
    }

    @Override
    public List<TrackingImage> getAllTrackingImages(Member member) {
        return imageRepository.findAllByMember(member);
    }
}
