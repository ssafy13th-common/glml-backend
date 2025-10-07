package com.ssafy.a705.diary.application;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.diary.domain.entity.DiaryImage;
import com.ssafy.a705.diary.domain.service.DiaryImageReader;
import com.ssafy.a705.diary.domain.service.DiaryImageStore;
import com.ssafy.a705.global.common.utils.S3PresignedUrlGenerator;
import com.ssafy.a705.global.common.utils.S3Uploader;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryImageApplicationService {

    private final DiaryImageStore imageStore;
    private final DiaryImageReader imageReader;
    private final S3PresignedUrlGenerator s3PresignedUploader;
    private final S3Uploader s3Uploader;

    @Transactional
    public void createDiaryImages(Diary diary, List<String> imageUrls) {
        List<DiaryImage> images = imageUrls.stream()
                .map(url -> DiaryImage.of(url, diary))
                .toList();

        imageStore.saveAll(images);
    }

    public String getDiaryThumbnailPresignedUrl(Diary diary) {
        Optional<DiaryImage> firstImage = diary.getDiaryImages().stream()
                .filter(diaryImage -> diaryImage.getDeletedAt() == null)
                .min(Comparator.comparing(DiaryImage::getId));
        return firstImage
                .map(diaryImage -> s3PresignedUploader.generatePresignedGetUrl(
                        diaryImage.getUrl())).orElse(null);
    }

    public List<String> getDiaryImageUrls(Diary diary) {
        List<DiaryImage> diaryImages = imageReader.findAllImageNotDeleted(
                diary);
        return diaryImages.stream()
                .map(diaryImage -> s3PresignedUploader.generatePresignedGetUrl(
                        diaryImage.getUrl())).toList();
    }

    @Transactional
    public void updateDiaryImageUrls(Diary diary, List<String> keepImageUrls,
            List<String> newImageUrls) {
        List<DiaryImage> existingImages = imageReader.findAllImageNotDeleted(
                diary);
        List<DiaryImage> toDeleteImages = existingImages.stream()
                .filter(image -> !keepImageUrls.contains(image.getUrl())).toList();
        toDeleteImages.forEach(diaryImage -> {
            diaryImage.deleteDiaryImage();
            s3Uploader.deleteFile(diaryImage.getUrl());
        });
        List<DiaryImage> images = newImageUrls.stream()
                .map(url -> DiaryImage.of(url, diary))
                .toList();
        imageStore.saveAll(images);
    }

    @Transactional
    public void deleteDiaryImages(Diary diary) {
        List<DiaryImage> diaryImages = imageReader.findAllImageNotDeleted(
                diary);
        diaryImages.forEach(diaryImage -> {
            diaryImage.deleteDiaryImage();
            s3Uploader.deleteFile(diaryImage.getUrl());
        });
        diary.deleteDiary();
    }

}
