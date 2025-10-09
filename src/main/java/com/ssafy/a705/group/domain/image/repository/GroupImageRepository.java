package com.ssafy.a705.group.domain.image.repository;

import com.ssafy.a705.group.domain.image.entity.GroupImage;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.repository.query.Param;

public interface GroupImageRepository {

    GroupImage save(GroupImage groupImage);
    Optional<GroupImage> findByIdNotDeleted(@Param("id") Long id);

    GroupImage getByIdNotDeleted(@NonNull Long id);
    List<GroupImage> findGroupImagesByGroupId(Long groupId,
                                              @Nullable Long cursorId, int pageSize);
}
