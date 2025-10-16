package com.ssafy.a705.location.domain.service;

import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.location.domain.entity.Location;
import com.ssafy.a705.location.domain.entity.LocationColor;
import java.util.List;
import java.util.Optional;

public interface LocationColorReader {

    Optional<LocationColor> findColorByLocation(Member member, Location location);

    LocationColor getColorByLocation(Member member, Location location);

    List<LocationColor> findAllLocationColors(Member member);

    boolean existsByLocationAndMember(Member member, Location location);
}
