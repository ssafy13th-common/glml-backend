package com.ssafy.a705.location.infrastructure.repository;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.location.domain.entity.Location;
import com.ssafy.a705.location.domain.entity.LocationColor;
import com.ssafy.a705.location.domain.repository.LocationColorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationColorJpaRepository extends JpaRepository<LocationColor, Long>,
        LocationColorRepository {

    @Override
    @Query("SELECT c FROM LocationColor c WHERE c.member = :member AND c.location = :location AND c.deletedAt IS NULL")
    Optional<LocationColor> findByMemberAndLocation(@Param("member") Member member,
            @Param("location") Location location);

    @Override
    @Query("SELECT c FROM LocationColor c WHERE c.member = :member AND c.deletedAt IS NULL")
    List<LocationColor> findAllByMember(Member member);

    @Override
    boolean existsByMemberAndLocation(Member member, Location location);
}
