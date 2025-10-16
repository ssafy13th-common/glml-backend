package com.ssafy.a705.location.infrastructure.service;

import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.location.domain.entity.Location;
import com.ssafy.a705.location.domain.entity.LocationColor;
import com.ssafy.a705.location.domain.exception.LocationColorNotFoundException;
import com.ssafy.a705.location.domain.repository.LocationColorRepository;
import com.ssafy.a705.location.domain.service.LocationColorReader;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationColorReaderImpl implements LocationColorReader {

    private final LocationColorRepository colorRepository;


    @Override
    public Optional<LocationColor> findColorByLocation(Member member, Location location) {
        return colorRepository.findByMemberAndLocation(member, location);
    }

    @Override
    public LocationColor getColorByLocation(Member member, Location location) {
        Optional<LocationColor> color = findColorByLocation(member, location);
        if (color.isEmpty()) {
            throw new LocationColorNotFoundException();
        }
        return color.get();
    }

    @Override
    public List<LocationColor> findAllLocationColors(Member member) {
        return colorRepository.findAllByMember(member);
    }

    @Override
    public boolean existsByLocationAndMember(Member member, Location location) {
        return colorRepository.existsByMemberAndLocation(member, location);
    }
}
