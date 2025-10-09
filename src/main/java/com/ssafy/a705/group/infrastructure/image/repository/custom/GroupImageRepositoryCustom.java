package com.ssafy.a705.group.infrastructure.image.repository.custom;

import com.ssafy.a705.group.domain.image.entity.GroupImage;
import jakarta.annotation.Nullable;
import java.util.List;

public interface GroupImageRepositoryCustom {

    List<GroupImage> findGroupImagesByGroupId(Long groupId,
            @Nullable Long cursorId, int pageSize);
}
