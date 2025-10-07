package com.ssafy.a705.location.infrastructure.repository;

import com.ssafy.a705.location.domain.entity.Location;
import com.ssafy.a705.location.domain.repository.LocationRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationJpaRepository extends JpaRepository<Location, Long>, LocationRepository {

    @Override
    Optional<Location> findByCode(Integer code);
}
