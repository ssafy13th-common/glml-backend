package com.ssafy.a705.diary.domain.repository;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.location.domain.entity.Location;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository {

    Diary save(Diary diary);

    Optional<Diary> findByIdAndDeletedAtIsNull(Long diaryId);

    boolean existsByLocationAndMember(Location location, Member member);

    int countByLocationAndMember(Location location, Member member);

    List<Diary> findDiariesWithThumbnailByMember(Member member, @Nullable Integer locationCode,
            @Nullable Long cursorId, int pageSize);
}
