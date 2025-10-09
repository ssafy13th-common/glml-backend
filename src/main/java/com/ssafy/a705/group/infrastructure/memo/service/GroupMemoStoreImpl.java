package com.ssafy.a705.group.infrastructure.memo.service;

import com.ssafy.a705.group.domain.memo.entity.GroupMemo;
import com.ssafy.a705.group.domain.memo.repository.GroupMemoRepository;
import com.ssafy.a705.group.domain.memo.service.GroupMemoStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMemoStoreImpl implements GroupMemoStore {
    private final GroupMemoRepository groupMemoRepository;
    @Override
    public void saveGroupMemo(GroupMemo groupMemo) {
        groupMemoRepository.save(groupMemo);
    }
}
