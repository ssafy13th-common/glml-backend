package com.ssafy.a705.diary.infrastructure.repository.custom;

import static com.ssafy.a705.diary.domain.entity.QDiary.diary;
import static com.ssafy.a705.location.domain.entity.QLocation.location;
import static com.ssafy.a705.location.domain.entity.QLocationColor.locationColor;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.member.domain.entity.Member;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiaryJpaRepositoryImpl implements DiaryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Diary> findDiariesWithThumbnailByMember(Member member,
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
                        diary.member.eq(member),
                        diary.deletedAt.isNull(),
                        locationCode != null ? diary.location.code.eq(locationCode) : null,
                        cursorId != null ? diary.id.lt(cursorId) : null
                )
                .orderBy(diary.id.desc())
                .limit(pageSize)
                .fetch();
    }
}
