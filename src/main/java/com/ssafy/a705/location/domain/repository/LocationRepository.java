package com.ssafy.a705.location.domain.repository;

import com.ssafy.a705.location.domain.entity.Location;
import java.util.Optional;

public interface LocationRepository {

    Location save(Location location);

    Optional<Location> findByCode(Integer code);
}
