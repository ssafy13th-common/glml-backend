package com.ssafy.a705.domain.group._image.repository;

import com.ssafy.a705.domain.group._image.entity.GroupImage;
import com.ssafy.a705.domain.group._image.exception.GroupImageNotFoundException;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupImageRepository extends JpaRepository<GroupImage, Long>,
        GroupImageRepositoryCustom {

    @Query("SELECT g FROM GroupImage g WHERE g.id = :id AND g.deletedAt IS NULL")
    Optional<GroupImage> findByIdNotDeleted(@Param("id") Long id);

    default @NonNull GroupImage getByIdNotDeleted(@NonNull Long id) {
        return findByIdNotDeleted(id).orElseThrow(GroupImageNotFoundException::new);
    }
}
