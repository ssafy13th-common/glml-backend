package com.ssafy.a705.diary.domain.service;

import com.ssafy.a705.diary.domain.entity.DiaryImage;
import java.util.List;

public interface DiaryImageStore {

    void saveAll(List<DiaryImage> images);

    DiaryImage save(DiaryImage image);

}
