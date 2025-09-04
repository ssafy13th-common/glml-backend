package com.ssafy.a705.domain.diary.repository;

import com.ssafy.a705.domain.diary.entity.Diary;
import com.ssafy.a705.domain.diary.exception.DiaryNotFoundException;
import com.ssafy.a705.domain.location.entity.Location;
import com.ssafy.a705.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiaryRepository extends JpaRepository<Diary, Long>, DiaryRepositoryCustom {

    @NonNull
    @Query("SELECT d FROM Diary d WHERE d.id = :diaryId AND d.deletedAt IS NULL")
    Optional<Diary> findById(@NonNull @Param("diaryId") Long diaryId);

    default @NonNull Diary getDiaryById(@NonNull Long diaryId) {
        return findById(diaryId).orElseThrow(DiaryNotFoundException::new);
    }

    boolean existsByLocationAndMember(Location location, Member member);

    @Query("SELECT count(d) FROM Diary d WHERE d.location = :location AND d.member = :member AND d.deletedAt IS NULL")
    int countByLocationAndMember(@Param("location") Location location,
            @Param("member") Member member);
}
