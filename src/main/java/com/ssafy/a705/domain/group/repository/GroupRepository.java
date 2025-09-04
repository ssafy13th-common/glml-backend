package com.ssafy.a705.domain.group.repository;

import com.ssafy.a705.domain.group.entity.Group;
import com.ssafy.a705.domain.group.entity.GroupStatus;
import com.ssafy.a705.domain.group.exception.GroupNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByIdAndDeletedAtIsNull(@NonNull Long groupId);

    @Query("SELECT g FROM GroupMember gm "
            + "JOIN gm.group g "
            + "WHERE gm.member.id = :memberId "
            + "AND gm.deletedAt IS NULL "
            + "AND g.deletedAt IS NULL")
    List<Group> findGroupsByMemberId(@Param("memberId") Long memberId);

    default Group getById(@NonNull Long groupId) {
        return findByIdAndDeletedAtIsNull(groupId).orElseThrow(GroupNotFoundException::new);
    }

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Group g set g.groupStatus = :status where g.startAt = :startAt")
    int updateStatusByStartAt(@Param("status") GroupStatus status, @Param("startAt") LocalDate startAt);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Group g set g.groupStatus = :status where g.endAt = :endAt")
    int updateStatusByEndAt(@Param("status") GroupStatus status, @Param("endAt") LocalDate endAt);

}
