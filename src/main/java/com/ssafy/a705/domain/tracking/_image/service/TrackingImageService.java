package com.ssafy.a705.domain.tracking._image.service;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.tracking._image.entity.TrackingImage;
import com.ssafy.a705.domain.tracking._image.repository.TrackingImageRepository;
import com.ssafy.a705.domain.tracking.dto.response.TrackingS3Url;
import com.ssafy.a705.global.common.utils.S3PresignedUploader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackingImageService {

    private final S3PresignedUploader uploader;
    private final TrackingImageRepository imageRepository;

    public void saveImage(String imageUrl, String trackingId, Member member) {
        imageRepository.findByMemberAndTrackingId(member, trackingId).ifPresentOrElse(
                value -> {
                    value.updateImage(imageUrl);
                },
                () -> {
                    TrackingImage image = TrackingImage.of(imageUrl, trackingId, member);
                    imageRepository.save(image);
                }
        );

    }

    public List<TrackingS3Url> getImages(Member member) {
        List<TrackingImage> images = imageRepository.findAllByMember(member);

        return images.stream()
                .map(t ->
                        new TrackingS3Url(
                                t.getTrackingId(),
                                uploader.generatePresignedGetUrl(t.getImageUrl())
                        )
                ).toList();
    }

    public void deleteImage(String trackingId, Member member) {
        imageRepository.findByMemberAndTrackingId(member, trackingId)
                .ifPresent(TrackingImage::deleteImage);
    }
}
