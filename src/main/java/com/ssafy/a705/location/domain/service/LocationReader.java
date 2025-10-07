package com.ssafy.a705.location.domain.service;

import com.ssafy.a705.location.domain.entity.Location;
import java.util.Optional;

public interface LocationReader {

    Optional<Location> findByCode(Integer code);

    Location getByCode(Integer code);
}
