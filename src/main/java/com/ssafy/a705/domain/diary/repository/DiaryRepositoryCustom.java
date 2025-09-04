package com.ssafy.a705.domain.diary.repository;

import com.ssafy.a705.domain.diary.entity.Diary;
import jakarta.annotation.Nullable;
import java.util.List;

public interface DiaryRepositoryCustom {

    List<Diary> findDiariesWithThumbnailByMemberId(
            Long memberId,
            @Nullable Integer locationCode,
            @Nullable Long cursorId,
            int pageSize);

}
