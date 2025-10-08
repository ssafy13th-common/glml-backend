package com.ssafy.a705.tracking.domain.repository;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.tracking.domain.entity.TrackingImage;
import java.util.List;
import java.util.Optional;

public interface TrackingImageRepository {

    TrackingImage save(TrackingImage image);

    Optional<TrackingImage> findByMemberAndTrackingId(Member member, String trackingId);

    List<TrackingImage> findAllByMember(Member member);

}
