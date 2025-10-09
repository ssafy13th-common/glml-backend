package com.ssafy.a705.group.domain.group.service;

import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.domain.group.entity.Group;
import java.util.List;

public interface GroupReader {
    Group getGroup(Long groupId);
    List<Group> getGroups(CustomUserDetails customUserDetails);
}
