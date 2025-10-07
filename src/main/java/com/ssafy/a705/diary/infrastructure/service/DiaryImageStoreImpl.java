package com.ssafy.a705.diary.infrastructure.service;

import com.ssafy.a705.diary.domain.entity.DiaryImage;
import com.ssafy.a705.diary.domain.repository.DiaryImageRepository;
import com.ssafy.a705.diary.domain.service.DiaryImageStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryImageStoreImpl implements DiaryImageStore {

    private final DiaryImageRepository imageRepository;

    @Override
    public void saveAll(List<DiaryImage> images) {
        imageRepository.saveAll(images);
    }

    @Override
    public DiaryImage save(DiaryImage image) {
        return imageRepository.save(image);
    }
}
