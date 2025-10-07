package com.ssafy.a705.diary.infrastructure.repository;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.diary.domain.entity.DiaryImage;
import com.ssafy.a705.diary.domain.repository.DiaryImageRepository;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiaryImageJpaRepository extends JpaRepository<DiaryImage, Long>,
        DiaryImageRepository {

    @Override
    @Query("SELECT d FROM DiaryImage d WHERE d.diary = :diary AND d.deletedAt IS NULL")
    List<DiaryImage> findAllByDiaryAndNotDeleted(@Param("diary") Diary diary);
}
