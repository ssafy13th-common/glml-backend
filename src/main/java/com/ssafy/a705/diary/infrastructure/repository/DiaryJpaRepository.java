package com.ssafy.a705.diary.infrastructure.repository;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.diary.domain.repository.DiaryRepository;
import com.ssafy.a705.diary.infrastructure.repository.custom.DiaryRepositoryCustom;
import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.location.domain.entity.Location;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiaryJpaRepository extends JpaRepository<Diary, Long>, DiaryRepository,
        DiaryRepositoryCustom {

    @Override
    Optional<Diary> findByIdAndDeletedAtIsNull(Long diaryId);

    @Override
    boolean existsByLocationAndMember(Location location, Member member);

    @Override
    @Query("SELECT count(d) FROM Diary d WHERE d.location = :location AND d.member = :member AND d.deletedAt IS NULL")
    int countByLocationAndMember(@Param("location") Location location,
            @Param("member") Member member);
}
