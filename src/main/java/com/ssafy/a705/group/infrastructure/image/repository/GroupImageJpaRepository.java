package com.ssafy.a705.group.infrastructure.image.repository;

import com.ssafy.a705.group.domain.image.entity.GroupImage;
import com.ssafy.a705.group.domain.image.exception.GroupImageNotFoundException;
import com.ssafy.a705.group.domain.image.repository.GroupImageRepository;
import com.ssafy.a705.group.infrastructure.image.repository.custom.GroupImageRepositoryCustom;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupImageJpaRepository extends JpaRepository<GroupImage, Long>, GroupImageRepositoryCustom,
        GroupImageRepository {

    @Query("SELECT g FROM GroupImage g WHERE g.id = :id AND g.deletedAt IS NULL")
    Optional<GroupImage> findByIdNotDeleted(@Param("id") Long id);

    default @NonNull GroupImage getByIdNotDeleted(@NonNull Long id) {
        return findByIdNotDeleted(id).orElseThrow(GroupImageNotFoundException::new);
    }
}
