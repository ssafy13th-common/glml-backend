package com.ssafy.a705.diary.infrastructure.service;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.diary.domain.repository.DiaryRepository;
import com.ssafy.a705.diary.domain.service.DiaryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryStoreImpl implements DiaryStore {

    private final DiaryRepository diaryRepository;

    @Override
    public Diary save(Diary diary) {
        return diaryRepository.save(diary);
    }
}
