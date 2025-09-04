package com.ssafy.a705.domain.group._memo.dto.response;

import com.ssafy.a705.domain.group._memo.entity.GroupMemo;
import java.util.List;

public record GroupMemosRes(
        Long groupId,
        List<GroupMemoRes> memos
) {

    public static GroupMemosRes of(Long groupId, List<GroupMemo> memos) {
        List<GroupMemoRes> memoResList = memos.stream().map(GroupMemoRes::from).toList();
        return new GroupMemosRes(groupId, memoResList);
    }
}
