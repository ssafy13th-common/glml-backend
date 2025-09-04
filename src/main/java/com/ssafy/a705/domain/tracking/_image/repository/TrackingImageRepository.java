package com.ssafy.a705.domain.tracking._image.repository;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.tracking._image.entity.TrackingImage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrackingImageRepository extends JpaRepository<TrackingImage, Long> {

    Optional<TrackingImage> findByMemberAndTrackingId(Member member, String trackingId);
    
    @Query("SELECT t FROM TrackingImage t WHERE t.deletedAt IS NULL AND t.member = :member")
    List<TrackingImage> findAllByMember(Member member);
}
