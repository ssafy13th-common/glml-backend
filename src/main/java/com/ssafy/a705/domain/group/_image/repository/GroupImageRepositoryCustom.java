package com.ssafy.a705.domain.group._image.repository;

import com.ssafy.a705.domain.group._image.entity.GroupImage;
import jakarta.annotation.Nullable;
import java.util.List;

public interface GroupImageRepositoryCustom {

    List<GroupImage> findGroupImagesByGroupId(Long groupId,
            @Nullable Long cursorId, int pageSize);
}
