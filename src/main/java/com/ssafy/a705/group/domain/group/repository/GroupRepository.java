package com.ssafy.a705.group.domain.group.repository;

import com.ssafy.a705.group.domain.group.entity.Group;
import com.ssafy.a705.group.domain.group.entity.GroupStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.repository.query.Param;

public interface GroupRepository {
    Group save(Group group);
    Optional<Group> findByIdAndDeletedAtIsNull(@NonNull Long groupId);
    List<Group> findGroupsByMemberId(@Param("memberId") Long memberId);
    int updateStatusByStartAt(@Param("status") GroupStatus status, @Param("startAt") LocalDate startAt);
    int updateStatusByEndAt(@Param("status") GroupStatus status, @Param("endAt") LocalDate endAt);
    Group getById(@NonNull Long groupId);
}
