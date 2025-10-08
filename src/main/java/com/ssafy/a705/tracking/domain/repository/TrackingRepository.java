package com.ssafy.a705.tracking.domain.repository;

import com.ssafy.a705.tracking.domain.entity.Tracking;
import java.util.Optional;

public interface TrackingRepository {

    Tracking save(Tracking tracking);

    Optional<Tracking> findByIdAndNotDeleted(String id);
}
