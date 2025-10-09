package com.ssafy.a705.group.domain.memo.service;

import com.ssafy.a705.group.domain.memo.entity.GroupMemo;
import java.util.List;

public interface GroupMemoReader {

    List<GroupMemo> getAllByGroupId(Long groupId);
    GroupMemo getById(Long memoId);

}
