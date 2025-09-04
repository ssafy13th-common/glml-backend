package com.ssafy.a705.domain.tracking.repository;

import com.ssafy.a705.domain.tracking.entity.Tracking;
import com.ssafy.a705.domain.tracking.exception.TrackingNotFoundException;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TrackingRepository extends MongoRepository<Tracking, String> {

    @Query("{ '_id': ?0, 'deletedAt': null }")
    Optional<Tracking> findByIdAndNotDeleted(String id);

    default @NonNull Tracking getById(@NonNull String id) {
        return findByIdAndNotDeleted(id).orElseThrow(TrackingNotFoundException::new);
    }
}
