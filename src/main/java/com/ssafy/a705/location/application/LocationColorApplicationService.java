package com.ssafy.a705.location.application;

import com.ssafy.a705.domain.diary.exception.DiaryNotFoundException;
import com.ssafy.a705.domain.diary.repository.DiaryRepository;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.location.domain.entity.Location;
import com.ssafy.a705.location.domain.entity.LocationColor;
import com.ssafy.a705.location.domain.exception.HexCodeNotInvalidException;
import com.ssafy.a705.location.domain.service.LocationColorReader;
import com.ssafy.a705.location.domain.service.LocationColorStore;
import com.ssafy.a705.location.domain.service.LocationReader;
import com.ssafy.a705.location.presentation.dto.request.ColorUpdateReq;
import com.ssafy.a705.location.presentation.dto.response.MapColorRes;
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
public class LocationColorApplicationService {

    private final LocationReader locationReader;
    private final LocationColorStore colorStore;
    private final LocationColorReader colorReader;
    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public void updateLocationColor(Integer locationId, ColorUpdateReq colorReq,
            CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Location location = locationReader.getByCode(locationId);

        checkDiary(location, member);
        isValidHex(colorReq.color());

        Optional<LocationColor> color = colorReader.findColorByLocation(member, location);
        if (color.isEmpty()) {
            LocationColor locationColor = LocationColor.of(colorReq.color(), member, location);
            colorStore.save(locationColor);
            return;
        }

        color.get().updateColor(colorReq);
    }

    @Transactional(readOnly = true)
    public MapColorRes getLocationColors(CustomUserDetails userDetails) {
        if (Objects.isNull(userDetails)) {
            return MapColorRes.from(new ArrayList<>());
        }

        Member member = memberRepository.getById(userDetails.getId());
        List<LocationColor> colors = colorReader.findAllLocationColors(member);
        return MapColorRes.from(colors);
    }

    private void checkDiary(Location location, Member member) {
        if (!diaryRepository.existsByLocationAndMember(location, member)) {
            throw new DiaryNotFoundException();
        }
    }

    private void isValidHex(String color) {
        String regex = "^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

        if (!color.matches(regex)) {
            throw new HexCodeNotInvalidException();
        }
    }
}