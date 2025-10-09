package com.ssafy.a705.group.infrastructure.group.service;

import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.domain.group.entity.Group;
import com.ssafy.a705.group.domain.group.repository.GroupRepository;
import com.ssafy.a705.group.domain.group.service.GroupReader;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupReaderImpl implements GroupReader {

    private final GroupRepository groupRepository;

    @Override
    public Group getGroup(Long groupId) {
        Group group = groupRepository.getById(groupId);
        return group;
    }

    @Override
    public List<Group> getGroups(CustomUserDetails userDetails) {
        List<Group> groups = groupRepository.findGroupsByMemberId(userDetails.getId());
        return groups;
    }
}
