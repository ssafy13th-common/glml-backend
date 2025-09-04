package com.ssafy.a705.domain.diary._image.service;

import com.ssafy.a705.domain.diary._image.entity.DiaryImage;
import com.ssafy.a705.domain.diary.entity.Diary;
import com.ssafy.a705.domain.diary.repository.DiaryImageRepository;
import com.ssafy.a705.global.common.utils.S3PresignedUploader;
import com.ssafy.a705.global.common.utils.S3Uploader;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryImageService {

    private final DiaryImageRepository diaryImageRepository;
    private final S3PresignedUploader s3PresignedUploader;
    private final S3Uploader s3Uploader;

    @Transactional
    public void createDiaryImages(Diary diary, List<String> imageUrls) {
        List<DiaryImage> images = imageUrls.stream()
                .map(url -> DiaryImage.of(url, diary))
                .toList();

        diaryImageRepository.saveAll(images);
    }

    public String getDiaryThumbnailPresignedUrl(Diary diary) {
        Optional<DiaryImage> firstImage = diary.getDiaryImages().stream()
                .filter(diaryImage -> diaryImage.getDeletedAt() == null)
                .min(Comparator.comparing(DiaryImage::getId));
        return firstImage
                .map(diaryImage -> s3PresignedUploader.generatePresignedGetUrl(
                        diaryImage.getImageUrl())).orElse(null);
    }

    public List<String> getDiaryImageUrls(Diary diary) {
        List<DiaryImage> diaryImages = diaryImageRepository.findAllByDiaryAndNotDeleted(
                diary);
        return diaryImages.stream()
                .map(diaryImage -> s3PresignedUploader.generatePresignedGetUrl(
                        diaryImage.getImageUrl())).toList();
    }

    @Transactional
    public void updateDiaryImageUrls(Diary diary, List<String> keepImageUrls,
            List<String> newImageUrls) {
        List<DiaryImage> existingImages = diaryImageRepository.findAllByDiaryAndNotDeleted(
                diary);
        List<DiaryImage> toDeleteImages = existingImages.stream()
                .filter(image -> !keepImageUrls.contains(image.getImageUrl())).toList();
        toDeleteImages.forEach(diaryImage -> {
            diaryImage.deleteDiaryImage();
            s3Uploader.deleteFile(diaryImage.getImageUrl());
        });
        List<DiaryImage> images = newImageUrls.stream()
                .map(url -> DiaryImage.of(url, diary))
                .toList();
        diaryImageRepository.saveAll(images);
    }

    @Transactional
    public void deleteDiaryImages(Diary diary) {
        List<DiaryImage> diaryImages = diaryImageRepository.findAllByDiaryAndNotDeleted(
                diary);
        diaryImages.forEach(diaryImage -> {
            diaryImage.deleteDiaryImage();
            s3Uploader.deleteFile(diaryImage.getImageUrl());
        });
        diary.deleteDiary();
    }

}
