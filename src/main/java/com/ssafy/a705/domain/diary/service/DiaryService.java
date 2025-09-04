package com.ssafy.a705.domain.diary.service;

import com.ssafy.a705.domain.diary._image.service.DiaryImageService;
import com.ssafy.a705.domain.diary.dto.request.DiaryCreateReq;
import com.ssafy.a705.domain.diary.dto.request.DiaryUpdateReq;
import com.ssafy.a705.domain.diary.dto.response.DiaryCreateRes;
import com.ssafy.a705.domain.diary.dto.response.DiaryDetailRes;
import com.ssafy.a705.domain.diary.dto.response.DiaryInfoRes;
import com.ssafy.a705.domain.diary.dto.response.DiaryInfosRes;
import com.ssafy.a705.domain.diary.entity.Diary;
import com.ssafy.a705.domain.diary.repository.DiaryRepository;
import com.ssafy.a705.domain.location._color.entity.Color;
import com.ssafy.a705.domain.location._color.entity.LocationColor;
import com.ssafy.a705.domain.location._color.repository.LocationColorRepository;
import com.ssafy.a705.domain.location.entity.Location;
import com.ssafy.a705.domain.location.repository.LocationRepository;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.domain.member.repository.MemberRepository;
import com.ssafy.a705.global.common.exception.ForbiddenException;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final MemberRepository memberRepository;
    private final DiaryRepository diaryRepository;
    private final LocationRepository locationRepository;
    private final DiaryImageService diaryImageService;
    private final LocationColorRepository colorRepository;

    @Transactional
    public DiaryCreateRes createDiary(DiaryCreateReq diaryReq, CustomUserDetails userDetails) {
        Member member = memberRepository.getById(userDetails.getId());
        Location location = locationRepository.getByCode(diaryReq.locationCode());
        Diary diary = Diary.from(diaryReq, member, location);
        diaryRepository.save(diary);
        diaryImageService.createDiaryImages(diary, diaryReq.imageUrls());

        if (!colorRepository.existsByLocationAndMember(location, member)) {
            Integer regionCode = getRegionCode(location.getCode());
            Color color = Color.fromCode(regionCode);
            colorRepository.save(LocationColor.of(color.getHexColor(), member, location));
        } else {
            int diaryCnt = diaryRepository.countByLocationAndMember(location, member);
            LocationColor color = colorRepository.getByMemberAndLocation(member, location);
            updateTransparency(diaryCnt, color);
        }
        return DiaryCreateRes.from(diary);
    }

    @Transactional(readOnly = true)
    public DiaryInfosRes getDiaries(CustomUserDetails userDetails, Integer locationCode,
            Long cursorId, int pageSize) {
        List<Diary> diaries = diaryRepository.findDiariesWithThumbnailByMemberId(
                userDetails.getId(), locationCode, cursorId, pageSize);
        return DiaryInfosRes.from(diaries.stream()
                .map(diary -> {
                    String presignedUrl = diaryImageService.getDiaryThumbnailPresignedUrl(diary);
                    return DiaryInfoRes.from(diary, presignedUrl);
                }).toList());
    }

    @Transactional(readOnly = true)
    public DiaryDetailRes getDiary(Long diaryId, CustomUserDetails userDetails) {
        Diary diary = diaryRepository.getDiaryById(diaryId);
        checkMemberCanAccess(userDetails.getId(), diary.getMember().getId());
        List<String> imageUrls = diaryImageService.getDiaryImageUrls(diary);
        return DiaryDetailRes.from(diary, imageUrls);
    }

    @Transactional
    public void updateDiary(Long diaryId, DiaryUpdateReq diaryReq,
            CustomUserDetails userDetails) {
        Diary diary = diaryRepository.getDiaryById(diaryId);
        checkMemberCanAccess(userDetails.getId(), diary.getMember().getId());
        diary.update(diaryReq.startedAt(), diaryReq.endedAt(), diaryReq.content());
        diaryImageService.updateDiaryImageUrls(diary, diaryReq.keepImageUrls(),
                diaryReq.newImageUrls());
    }

    @Transactional
    public void deleteDiary(Long diaryId, CustomUserDetails userDetails) {
        Diary diary = diaryRepository.getDiaryById(diaryId);
        checkMemberCanAccess(userDetails.getId(), diary.getMember().getId());

        int diaryCnt = diaryRepository.countByLocationAndMember(diary.getLocation(),
                diary.getMember());
        LocationColor color = colorRepository.getByMemberAndLocation(diary.getMember(),
                diary.getLocation());
        updateTransparency(Math.max(0, diaryCnt - 1), color);

        if (diaryCnt == 1) {
            color.deleteColor();
        }

        diaryImageService.deleteDiaryImages(diary);
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
