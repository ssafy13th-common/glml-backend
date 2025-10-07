package com.ssafy.a705.diary.domain.repository;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.diary.domain.entity.DiaryImage;
import java.util.List;

public interface DiaryImageRepository {

    DiaryImage save(DiaryImage image);

    <S extends DiaryImage> List<S> saveAll(Iterable<S> images);

    List<DiaryImage> findAllByDiaryAndNotDeleted(Diary diary);
}
