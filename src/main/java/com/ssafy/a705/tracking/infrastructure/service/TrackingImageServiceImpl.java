package com.ssafy.a705.tracking.infrastructure.service;

import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.global.common.utils.S3PresignedUrlGenerator;
import com.ssafy.a705.tracking.domain.entity.Tracking;
import com.ssafy.a705.tracking.domain.entity.TrackingImage;
import com.ssafy.a705.tracking.domain.service.TrackingImageReader;
import com.ssafy.a705.tracking.domain.service.TrackingImageService;
import com.ssafy.a705.tracking.domain.service.TrackingImageStore;
import com.ssafy.a705.tracking.presentation.dto.response.TrackingS3Url;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackingImageServiceImpl implements TrackingImageService {

    private final S3PresignedUrlGenerator uploader;
    private final TrackingImageStore imageStore;
    private final TrackingImageReader imageReader;

    @Override
    public void saveImage(String imageUrl, String trackingId, Member member) {
        imageReader.getTrackingImage(member, trackingId).ifPresentOrElse(
                value -> {
                    value.updateImage(imageUrl);
                },
                () -> {
                    TrackingImage image = TrackingImage.of(imageUrl, trackingId, member);
                    imageStore.save(image);
                }
        );

    }

    @Override
    public List<TrackingS3Url> getImages(Member member) {
        List<TrackingImage> images = imageReader.getAllTrackingImages(member);

        return images.stream()
                .map(t ->
                        new TrackingS3Url(
                                t.getTrackingId(),
                                uploader.generatePresignedGetUrl(t.getImageUrl())
                        )
                ).toList();
    }

    @Override
    public List<String> uploadImage(Tracking tracking) {
        return tracking.getImages().stream()
                .map(uploader::generatePresignedGetUrl)
                .toList();
    }

    @Override
    public void deleteImage(String trackingId, Member member) {
        imageReader.getTrackingImage(member, trackingId)
                .ifPresent(TrackingImage::deleteImage);
    }
}
