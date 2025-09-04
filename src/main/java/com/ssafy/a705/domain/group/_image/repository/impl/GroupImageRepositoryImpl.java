package com.ssafy.a705.domain.group._image.repository.impl;

import static com.ssafy.a705.domain.group._image.entity.QGroupImage.groupImage;
import static com.ssafy.a705.domain.group._member.entity.QGroupMember.groupMember;
import static com.ssafy.a705.domain.group.entity.QGroup.group;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.a705.domain.group._image.entity.GroupImage;
import com.ssafy.a705.domain.group._image.repository.GroupImageRepositoryCustom;
import jakarta.annotation.Nullable;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GroupImageRepositoryImpl implements GroupImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GroupImage> findGroupImagesByGroupId(Long groupId,
            @Nullable Long cursorId, int pageSize) {
        return queryFactory.selectFrom(groupImage)
                .join(groupImage.groupMember, groupMember)
                .join(groupMember.group, group)
                .where(
                        groupImage.groupMember.group.id.eq(groupId),
                        groupImage.deletedAt.isNull(),
                        cursorId != null ? groupImage.id.lt(cursorId) : null

                )
                .orderBy(groupImage.id.desc())
                .limit(pageSize)
                .fetch();
    }
}