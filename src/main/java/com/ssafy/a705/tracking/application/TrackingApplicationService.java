package com.ssafy.a705.tracking.application;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.tracking.domain.entity.Tracking;
import com.ssafy.a705.tracking.domain.service.TrackingImageService;
import com.ssafy.a705.tracking.domain.service.TrackingService;
import com.ssafy.a705.tracking.domain.service.TrackingStore;
import com.ssafy.a705.tracking.presentation.dto.request.TrackingCreateReq;
import com.ssafy.a705.tracking.presentation.dto.request.TrackingUpdateReq;
import com.ssafy.a705.tracking.presentation.dto.response.TrackingCreateRes;
import com.ssafy.a705.tracking.presentation.dto.response.TrackingDetailRes;
import com.ssafy.a705.tracking.presentation.dto.response.TrackingInfosRes;
import com.ssafy.a705.tracking.presentation.dto.response.TrackingS3Url;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TrackingApplicationService {

    private final TrackingStore trackingStore;
    private final TrackingService trackingService;
    private final TrackingImageService imageService;
    private final MemberRepository memberRepository;

    @Transactional
    public TrackingCreateRes createTracking(TrackingCreateReq trackingCreateReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Tracking tracking = Tracking.from(trackingCreateReq, userDetails);
        trackingStore.save(tracking);

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
        Tracking tracking = trackingService.getTracking(trackingId, userDetails);
        List<String> images = imageService.uploadImage(tracking);
        return TrackingDetailRes.from(tracking, images);
    }

    @Transactional
    public void updateTracking(String trackingId, TrackingUpdateReq trackingUpdateReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());

        Tracking tracking = trackingService.getTracking(trackingId, userDetails);
        tracking.update(trackingUpdateReq);
        trackingStore.save(tracking);

        imageService.saveImage(trackingUpdateReq.thumbnailImage(), trackingId, member);
    }

    @Transactional
    public void deleteTracking(String trackingId, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());

        Tracking tracking = trackingService.getTracking(trackingId, userDetails);
        tracking.delete();
        trackingStore.save(tracking);

        imageService.deleteImage(trackingId, member);
    }
    
}
