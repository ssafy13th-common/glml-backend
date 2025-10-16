package com.ssafy.a705.group.infrastructure.image.repository.custom;


import static com.ssafy.a705.group.domain.group.entity.QGroup.group;
import static com.ssafy.a705.group.domain.image.entity.QGroupImage.groupImage;
import static com.ssafy.a705.group.domain.participant.entity.QParticipant.participant;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.a705.group.domain.image.entity.GroupImage;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupImageJpaRepositoryImpl implements GroupImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GroupImage> findGroupImagesByGroupId(Long groupId,
            @Nullable Long cursorId, int pageSize) {
        return queryFactory.selectFrom(groupImage)
                .join(groupImage.participant, participant)
                .join(participant.group, group)
                .where(
                        groupImage.participant.group.id.eq(groupId),
                        groupImage.deletedAt.isNull(),
                        cursorId != null ? groupImage.id.lt(cursorId) : null

                )
                .orderBy(groupImage.id.desc())
                .limit(pageSize)
                .fetch();
    }
}