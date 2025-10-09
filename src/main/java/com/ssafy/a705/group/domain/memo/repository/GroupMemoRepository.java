package com.ssafy.a705.group.domain.memo.repository;

import com.ssafy.a705.group.domain.memo.entity.GroupMemo;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.repository.query.Param;

public interface GroupMemoRepository {
    GroupMemo save(GroupMemo groupMemo);

    Optional<GroupMemo> findByIdAndDeletedAtIsNull(@NonNull Long id);
    List<GroupMemo> findAllByGroupIdAndDeletedAtIsNull(@Param("groupId") Long groupId);

    GroupMemo getGroupMemoById(@NonNull Long id);

}
