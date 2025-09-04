package com.ssafy.a705.domain.diary.repository;

import com.ssafy.a705.domain.diary._image.entity.DiaryImage;
import com.ssafy.a705.domain.diary.entity.Diary;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiaryImageRepository extends JpaRepository<DiaryImage, Long> {

    @Query("SELECT d FROM DiaryImage d WHERE d.diary = :diary AND d.deletedAt IS NULL")
    List<DiaryImage> findAllByDiaryAndNotDeleted(@Param("diary") Diary diary);
}
