package com.ssafy.a705.domain.location.repository;

import com.ssafy.a705.domain.location.entity.Location;
import com.ssafy.a705.domain.location.exception.LocationNotFoundException;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByCode(Integer code);

    default @NonNull Location getByCode(@NonNull Integer code) {
        return findByCode(code).orElseThrow(LocationNotFoundException::new);
    }
}
