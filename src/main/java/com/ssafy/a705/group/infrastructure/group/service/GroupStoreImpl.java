package com.ssafy.a705.group.infrastructure.group.service;

import com.ssafy.a705.group.domain.group.entity.Group;
import com.ssafy.a705.group.domain.group.repository.GroupRepository;
import com.ssafy.a705.group.domain.group.service.GroupStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GroupStoreImpl implements GroupStore {

    private final GroupRepository groupRepository;

    @Override
    public void saveGroup(Group group) {
        groupRepository.save(group);
    }
}
