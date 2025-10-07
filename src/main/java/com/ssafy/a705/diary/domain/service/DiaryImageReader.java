package com.ssafy.a705.diary.domain.service;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.diary.domain.entity.DiaryImage;
import java.util.List;

public interface DiaryImageReader {

    List<DiaryImage> findAllImageNotDeleted(Diary diary);

}
