package com.ssafy.a705.tracking.infrastructure.repository;

import com.ssafy.a705.tracking.domain.entity.Tracking;
import com.ssafy.a705.tracking.domain.repository.TrackingRepository;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TrackingJpaRepository extends MongoRepository<Tracking, String>,
        TrackingRepository {

    @Query("{ '_id': ?0, 'deletedAt': null }")
    Optional<Tracking> findByIdAndNotDeleted(String id);
}
