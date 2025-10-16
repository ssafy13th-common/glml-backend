package com.ssafy.a705.diary.infrastructure.repository.custom;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.member.domain.entity.Member;
import jakarta.annotation.Nullable;
import java.util.List;

public interface DiaryRepositoryCustom {

    List<Diary> findDiariesWithThumbnailByMember(
            Member member,
            @Nullable Integer locationCode,
            @Nullable Long cursorId,
            int pageSize);

}
