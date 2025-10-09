package com.ssafy.a705.group.infrastructure.group.repository;

import com.ssafy.a705.group.domain.group.entity.Group;
import com.ssafy.a705.group.domain.group.entity.GroupStatus;
import com.ssafy.a705.group.domain.group.exception.GroupNotFoundException;
import com.ssafy.a705.group.domain.group.repository.GroupRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GroupJpaRepository extends JpaRepository<Group, Long>, GroupRepository {
    @Override
    Optional<Group> findByIdAndDeletedAtIsNull(@NonNull Long groupId);

    @Override
    @Query("SELECT g FROM Participant p "
            + "JOIN p.group g "
            + "WHERE p.member.id = :memberId "
            + "AND p.deletedAt IS NULL "
            + "AND g.deletedAt IS NULL")
    List<Group> findGroupsByMemberId(@Param("memberId") Long memberId);

    @Override
    default Group getById(@NonNull Long groupId) {
        return findByIdAndDeletedAtIsNull(groupId).orElseThrow(GroupNotFoundException::new);
    }

    @Override
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Group g set g.status = :status where g.startAt = :startAt")
    int updateStatusByStartAt(@Param("status") GroupStatus status, @Param("startAt") LocalDate startAt);

    @Override
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Group g set g.status = :status where g.endAt = :endAt")
    int updateStatusByEndAt(@Param("status") GroupStatus status, @Param("endAt") LocalDate endAt);

}
