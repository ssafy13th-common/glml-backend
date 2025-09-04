package com.ssafy.a705.domain.location._color.service;

import com.ssafy.a705.domain.diary.repository.DiaryRepository;
import com.ssafy.a705.domain.location._color.dto.request.ColorUpdateReq;
import com.ssafy.a705.domain.location._color.dto.response.MapColorRes;
import com.ssafy.a705.domain.location._color.entity.LocationColor;
import com.ssafy.a705.domain.location._color.exception.DiaryNotWrittenException;
import com.ssafy.a705.domain.location._color.exception.HexCodeNotInvalidException;
import com.ssafy.a705.domain.location._color.repository.LocationColorRepository;
import com.ssafy.a705.domain.location.entity.Location;
import com.ssafy.a705.domain.location.repository.LocationRepository;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationColorService {

    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;
    private final LocationColorRepository colorRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public void updateLocationColor(Integer locationId, ColorUpdateReq colorReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Location location = locationRepository.getByCode(locationId);
        Optional<LocationColor> color = colorRepository.findByMemberAndLocation(member, location);
        if (!diaryRepository.existsByLocationAndMember(location, member)) {
            log.info("다이어리를 작성해야 색을 바꿀 수 있습니다.");
            throw new DiaryNotWrittenException();
        }
        if (color.isEmpty()) {
            LocationColor locationColor = LocationColor.of(colorReq.color(), member, location);
            colorRepository.save(locationColor);
            return;
        }

        if (!isValidHex(colorReq.color())) {
            throw new HexCodeNotInvalidException();
        }
        color.get().updateColor(colorReq);
    }

    @Transactional(readOnly = true)
    public MapColorRes getLocationColors(CustomUserDetails userDetails) {
        if (Objects.isNull(userDetails)) {
            return MapColorRes.from(new ArrayList<>());
        }

        Member member = memberRepository.getById(userDetails.getId());
        List<LocationColor> colors = colorRepository.findAllByMember(member);
        return MapColorRes.from(colors);
    }

    private boolean isValidHex(String color) {
        String regex = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

        return color.matches(regex);
    }
}