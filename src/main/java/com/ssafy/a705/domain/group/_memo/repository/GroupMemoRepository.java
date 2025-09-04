package com.ssafy.a705.domain.group._memo.repository;

import com.ssafy.a705.domain.group._memo.entity.GroupMemo;
import com.ssafy.a705.domain.group._memo.exception.MemoNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupMemoRepository extends JpaRepository<GroupMemo, Long> {

    Optional<GroupMemo> findByIdAndDeletedAtIsNull(@NonNull Long id);

    @Query("SELECT gm FROM GroupMemo gm "
            + "JOIN FETCH gm.groupMember mem "
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
