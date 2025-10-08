package com.ssafy.a705.tracking.infrastructure.repository;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.tracking.domain.entity.TrackingImage;
import com.ssafy.a705.tracking.domain.repository.TrackingImageRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrackingImageJpaRepository extends JpaRepository<TrackingImage, Long>,
        TrackingImageRepository {

    Optional<TrackingImage> findByMemberAndTrackingId(Member member, String trackingId);

    @Query("SELECT t FROM TrackingImage t WHERE t.deletedAt IS NULL AND t.member = :member")
    List<TrackingImage> findAllByMember(Member member);
}