package com.ssafy.a705.group.infrastructure.memo.repository;

import com.ssafy.a705.group.domain.memo.entity.GroupMemo;
import com.ssafy.a705.group.domain.memo.exception.MemoNotFoundException;
import com.ssafy.a705.group.domain.memo.repository.GroupMemoRepository;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupMemoJpaRepository extends JpaRepository<GroupMemo, Long>, GroupMemoRepository {

    Optional<GroupMemo> findByIdAndDeletedAtIsNull(@NonNull Long id);

    @Query("SELECT gm FROM GroupMemo gm "
            + "JOIN FETCH gm.participant mem "
            + "JOIN FETCH mem.group g "
            + "WHERE g.id = :groupId "
            + "AND gm.deletedAt IS NULL "
            + "AND mem.deletedAt IS NULL "
            + "AND g.deletedAt IS NULL ")
    List<GroupMemo> findAllByGroupIdAndDeletedAtIsNull(@Param("groupId") Long groupId);

    default @NonNull GroupMemo getGroupMemoById(@NonNull Long id) {
        return findByIdAndDeletedAtIsNull(id).orElseThrow(MemoNotFoundException::new);
    }

}
