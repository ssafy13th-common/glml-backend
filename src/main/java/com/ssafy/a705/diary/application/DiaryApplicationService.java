package com.ssafy.a705.diary.application;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.diary.domain.service.DiaryReader;
import com.ssafy.a705.diary.domain.service.DiaryStore;
import com.ssafy.a705.diary.presentation.dto.request.DiaryCreateReq;
import com.ssafy.a705.diary.presentation.dto.request.DiaryUpdateReq;
import com.ssafy.a705.diary.presentation.dto.response.DiaryCreateRes;
import com.ssafy.a705.diary.presentation.dto.response.DiaryDetailRes;
import com.ssafy.a705.diary.presentation.dto.response.DiaryInfoRes;
import com.ssafy.a705.diary.presentation.dto.response.DiaryInfosRes;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.location.domain.entity.Color;
import com.ssafy.a705.location.domain.entity.Location;
import com.ssafy.a705.location.domain.entity.LocationColor;
import com.ssafy.a705.location.domain.service.LocationColorReader;
import com.ssafy.a705.location.domain.service.LocationColorStore;
import com.ssafy.a705.location.domain.service.LocationReader;
import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.member.domain.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryApplicationService {

    private final LocationReader locationReader;
    private final LocationColorReader colorReader;
    private final LocationColorStore colorStore;
    private final MemberService memberService;
    private final DiaryReader diaryReader;
    private final DiaryStore diaryStore;
    private final DiaryImageApplicationService imageService;

    @Transactional
    public DiaryCreateRes createDiary(DiaryCreateReq diaryReq, CustomUserDetails userDetails) {
        Member member = memberService.getMember(userDetails.getEmail());
        Location location = locationReader.getByCode(diaryReq.locationCode());
        Diary diary = Diary.from(diaryReq, member, location);
        diaryStore.save(diary);
        imageService.createDiaryImages(diary, diaryReq.imageUrls());

        if (!colorReader.existsByLocationAndMember(member, location)) {
            Integer regionCode = getRegionCode(location.getCode());
            Color color = Color.fromCode(regionCode);
            colorStore.save(LocationColor.of(color.getHexColor(), member, location));
        } else {
            int diaryCnt = diaryReader.countDiary(location, member);
            LocationColor color = colorReader.getColorByLocation(member, location);
            updateTransparency(diaryCnt, color);
        }
        return DiaryCreateRes.from(diary);
    }

    @Transactional(readOnly = true)
    public DiaryInfosRes getDiaries(CustomUserDetails userDetails, Integer locationCode,
            Long cursorId, int pageSize) {
        Member member = memberService.getMember(userDetails.getEmail());
        List<Diary> diaries = diaryReader.getAllDiaries(member, locationCode, cursorId,
                pageSize);
        return DiaryInfosRes.from(diaries.stream()
                .map(diary -> {
                    String presignedUrl = imageService.getDiaryThumbnailPresignedUrl(diary);
                    return DiaryInfoRes.from(diary, presignedUrl);
                }).toList());
    }

    @Transactional(readOnly = true)
    public DiaryDetailRes getDiary(Long diaryId, CustomUserDetails userDetails) {
        Diary diary = diaryReader.getDiary(diaryId);
        checkMemberCanAccess(userDetails.getId(), diary.getMember().getId());
        List<String> imageUrls = imageService.getDiaryImageUrls(diary);
        return DiaryDetailRes.from(diary, imageUrls);
    }

    @Transactional
    public void updateDiary(Long diaryId, DiaryUpdateReq diaryReq,
            CustomUserDetails userDetails) {
        Diary diary = diaryReader.getDiary(diaryId);
        checkMemberCanAccess(userDetails.getId(), diary.getMember().getId());
        diary.update(diaryReq.startedAt(), diaryReq.endedAt(), diaryReq.content());
        imageService.updateDiaryImageUrls(diary, diaryReq.keepImageUrls(),
                diaryReq.newImageUrls());
    }

    @Transactional
    public void deleteDiary(Long diaryId, CustomUserDetails userDetails) {
        Diary diary = diaryReader.getDiary(diaryId);
        checkMemberCanAccess(userDetails.getId(), diary.getMember().getId());

        int diaryCnt = diaryReader.countDiary(diary.getLocation(), diary.getMember());
        LocationColor color = colorReader.getColorByLocation(diary.getMember(),
                diary.getLocation());
        updateTransparency(Math.max(0, diaryCnt - 1), color);

        if (diaryCnt == 1) {
            color.deleteColor();
        }

        imageService.deleteDiaryImages(diary);
    }

    private void checkMemberCanAccess(Long accessMemberId, Long createMemberId) {
        if (!accessMemberId.equals(createMemberId)) {
            throw new ForbiddenException("다이어리 접근");
        }
    }

    private Integer getRegionCode(Integer locationCode) {
        return locationCode / 1000;
    }

    private void updateTransparency(int diaryCnt, LocationColor color) {
        switch (diaryCnt) {
            case 1:
                break;
            case 2:
                color.updateTransparency("99");
                break;
            case 3:
                color.updateTransparency("CC");
                break;
            default:
                color.updateTransparency("FF");
        }
    }
}
