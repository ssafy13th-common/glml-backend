package com.ssafy.a705.domain.diary.repository.impl;

import static com.ssafy.a705.domain.diary.entity.QDiary.diary;
import static com.ssafy.a705.domain.location._color.entity.QLocationColor.locationColor;
import static com.ssafy.a705.domain.location.entity.QLocation.location;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.a705.domain.diary.entity.Diary;
import com.ssafy.a705.domain.diary.repository.DiaryRepositoryCustom;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiaryRepositoryImpl implements DiaryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Diary> findDiariesWithThumbnailByMemberId(Long memberId,
            @Nullable Integer locationCode, @Nullable Long cursorId, int pageSize) {

        return queryFactory
                .selectFrom(diary)
                .leftJoin(diary.location, location).fetchJoin()
                .leftJoin(locationColor)
                .on(locationColor.location.eq(diary.location)
                        .and(locationColor.member.eq(diary.member))
                        .and(locationColor.deletedAt.isNull())
                )
                .where(
                        diary.member.id.eq(memberId),
                        diary.deletedAt.isNull(),
                        locationCode != null ? diary.location.code.eq(locationCode) : null,
                        cursorId != null ? diary.id.lt(cursorId) : null
                )
                .orderBy(diary.id.desc())
                .limit(pageSize)
                .fetch();
    }
}
