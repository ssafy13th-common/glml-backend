package com.ssafy.a705.domain.location._color.repository;

import com.ssafy.a705.domain.location._color.entity.LocationColor;
import com.ssafy.a705.domain.location.entity.Location;
import com.ssafy.a705.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationColorRepository extends JpaRepository<LocationColor, Long> {

    @Query("SELECT c FROM LocationColor c WHERE c.member = :member AND c.location = :location AND c.deletedAt IS NULL")
    Optional<LocationColor> findByMemberAndLocation(@Param("member") Member member,
            @Param("location") Location location);

    default @NonNull LocationColor getByMemberAndLocation(Member member, Location location) {
        return findByMemberAndLocation(member, location).orElseThrow();
    }

    @Query("SELECT c FROM LocationColor c WHERE c.member = :member AND c.deletedAt IS NULL")
    List<LocationColor> findAllByMember(Member member);

    boolean existsByLocationAndMember(Location location, Member member);
}
