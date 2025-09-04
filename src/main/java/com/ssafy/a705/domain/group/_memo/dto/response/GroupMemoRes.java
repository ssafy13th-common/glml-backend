package com.ssafy.a705.domain.group._memo.dto.response;

import com.ssafy.a705.domain.group._memo.entity.GroupMemo;

public record GroupMemoRes(
        Long memoId,
        String content,
        String writer) {

    public static GroupMemoRes from(GroupMemo groupMemo) {
        return new GroupMemoRes(groupMemo.getId(), groupMemo.getContent(),
                groupMemo.getGroupMember().getMember().getNickname());
    }
}
