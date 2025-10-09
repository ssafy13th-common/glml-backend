package com.ssafy.a705.group.infrastructure.memo.service;

import com.ssafy.a705.group.domain.memo.entity.GroupMemo;
import com.ssafy.a705.group.domain.memo.repository.GroupMemoRepository;
import com.ssafy.a705.group.domain.memo.service.GroupMemoReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMemoReaderImpl implements GroupMemoReader {

    private final GroupMemoRepository groupMemoRepository;

    @Override
    public List<GroupMemo> getAllByGroupId(Long groupId) {
        return groupMemoRepository.findAllByGroupIdAndDeletedAtIsNull(groupId);
    }

    @Override
    public GroupMemo getById(Long memoId) {
        return groupMemoRepository.getGroupMemoById(memoId);
    }
}
