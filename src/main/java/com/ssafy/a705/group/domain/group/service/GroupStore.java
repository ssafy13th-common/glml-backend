package com.ssafy.a705.group.domain.group.service;

import com.ssafy.a705.group.domain.group.entity.Group;
import com.ssafy.a705.group.presentation.group.dto.request.GroupReq;

public interface GroupStore {
    void saveGroup(Group group);
}
