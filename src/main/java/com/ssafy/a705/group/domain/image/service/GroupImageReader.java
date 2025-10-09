package com.ssafy.a705.group.domain.image.service;

import com.ssafy.a705.group.domain.image.entity.GroupImage;
import com.ssafy.a705.group.presentation.image.dto.response.GroupImageRes;
import java.util.List;

public interface GroupImageReader {
    List<GroupImageRes> getGroupImages(Long groupId, Long cursorId, int pageSize);
    GroupImage getByIdNotDeleted(Long groupImageId);
}
