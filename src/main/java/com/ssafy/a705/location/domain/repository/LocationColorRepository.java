package com.ssafy.a705.location.domain.repository;

import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.location.domain.entity.Location;
import com.ssafy.a705.location.domain.entity.LocationColor;
import java.util.List;
import java.util.Optional;

public interface LocationColorRepository {

    LocationColor save(LocationColor color);

    Optional<LocationColor> findByMemberAndLocation(Member member, Location location);

    List<LocationColor> findAllByMember(Member member);

    boolean existsByMemberAndLocation(Member member, Location location);
}
