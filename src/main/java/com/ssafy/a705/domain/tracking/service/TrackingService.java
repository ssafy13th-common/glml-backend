package com.ssafy.a705.domain.tracking.service;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.domain.tracking._image.service.TrackingImageService;
import com.ssafy.a705.domain.tracking.dto.request.TrackingCreateReq;
import com.ssafy.a705.domain.tracking.dto.request.TrackingUpdateReq;
import com.ssafy.a705.domain.tracking.dto.response.TrackingCreateRes;
import com.ssafy.a705.domain.tracking.dto.response.TrackingDetailRes;
import com.ssafy.a705.domain.tracking.dto.response.TrackingInfosRes;
import com.ssafy.a705.domain.tracking.dto.response.TrackingS3Url;
import com.ssafy.a705.domain.tracking.entity.Tracking;
import com.ssafy.a705.domain.tracking.repository.TrackingRepository;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.common.utils.S3PresignedUploader;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrackingService {

    private final TrackingImageService imageService;
    private final MemberRepository memberRepository;
    private final TrackingRepository trackingRepository;
    private final S3PresignedUploader uploader;

    @Transactional
    public TrackingCreateRes createTracking(TrackingCreateReq trackingCreateReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Tracking tracking = Tracking.from(trackingCreateReq, userDetails);
        trackingRepository.save(tracking);

        imageService.saveImage(trackingCreateReq.thumbnailImage(), tracking.getId(), member);
        return TrackingCreateRes.of(tracking.getId());
    }

    @Transactional(readOnly = true)
    public TrackingInfosRes getTrackingInfos(CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        List<TrackingS3Url> trackingImages = imageService.getImages(member);
        return TrackingInfosRes.of(trackingImages);
    }

    @Transactional(readOnly = true)
    public TrackingDetailRes getTrackingDetail(String trackingId, CustomUserDetails userDetails) {
        Tracking tracking = trackingRepository.getById(trackingId);
        checkUserHaveTracking(userDetails, tracking);
        List<String> images = tracking.getImages().stream()
                .map(uploader::generatePresignedGetUrl)
                .toList();
        return TrackingDetailRes.from(tracking, images);
    }

    @Transactional
    public void updateTracking(String trackingId, TrackingUpdateReq trackingUpdateReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());

        Tracking tracking = trackingRepository.getById(trackingId);
        checkUserHaveTracking(userDetails, tracking);
        tracking.update(trackingUpdateReq);
        trackingRepository.save(tracking);

        imageService.saveImage(trackingUpdateReq.thumbnailImage(), trackingId, member);
    }

    @Transactional
    public void deleteTracking(String trackingId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());

        Tracking tracking = trackingRepository.getById(trackingId);
        checkUserHaveTracking(userDetails, tracking);
        tracking.delete();
        trackingRepository.save(tracking);

        imageService.deleteImage(trackingId, member);
    }

    private void checkUserHaveTracking(CustomUserDetails userDetails, Tracking tracking) {
        if (Objects.equals(userDetails.getId(), tracking.getUserId())) {
            return;
        }
        throw new ForbiddenException("트래킹 접근");
    }
}
