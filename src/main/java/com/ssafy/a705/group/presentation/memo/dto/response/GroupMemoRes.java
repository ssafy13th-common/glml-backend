package com.ssafy.a705.group.presentation.memo.dto.response;

import com.ssafy.a705.group.domain.memo.entity.GroupMemo;

public record GroupMemoRes(
        Long memoId,
        String content,
        String writer) {

    public static GroupMemoRes from(GroupMemo groupMemo) {
        return new GroupMemoRes(groupMemo.getId(), groupMemo.getContent(),
                groupMemo.getParticipant().getMember().getNickname());
    }
}
