package com.ssafy.a705.tracking.domain.service;

import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.tracking.domain.entity.TrackingImage;
import java.util.List;
import java.util.Optional;

public interface TrackingImageReader {

    Optional<TrackingImage> getTrackingImage(Member member, String trackingId);

    List<TrackingImage> getAllTrackingImages(Member member);

}
