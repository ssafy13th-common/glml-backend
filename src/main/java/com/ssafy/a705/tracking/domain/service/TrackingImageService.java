package com.ssafy.a705.tracking.domain.service;

import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.tracking.domain.entity.Tracking;
import com.ssafy.a705.tracking.presentation.dto.response.TrackingS3Url;
import java.util.List;

public interface TrackingImageService {

    void saveImage(String imageUrl, String trackingId, Member member);

    List<TrackingS3Url> getImages(Member member);

    List<String> uploadImage(Tracking tracking);

    void deleteImage(String trackingId, Member member);
}
