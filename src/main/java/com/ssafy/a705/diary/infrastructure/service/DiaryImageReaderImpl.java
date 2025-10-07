package com.ssafy.a705.diary.infrastructure.service;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.diary.domain.entity.DiaryImage;
import com.ssafy.a705.diary.domain.repository.DiaryImageRepository;
import com.ssafy.a705.diary.domain.service.DiaryImageReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryImageReaderImpl implements DiaryImageReader {

    private final DiaryImageRepository imageRepository;

    @Override
    public List<DiaryImage> findAllImageNotDeleted(Diary diary) {
        return imageRepository.findAllByDiaryAndNotDeleted(diary);
    }
}
