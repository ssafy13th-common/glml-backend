package com.ssafy.a705.diary.infrastructure.service;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.diary.domain.exception.DiaryNotFoundException;
import com.ssafy.a705.diary.domain.repository.DiaryRepository;
import com.ssafy.a705.diary.domain.service.DiaryReader;
import com.ssafy.a705.domain.member.entity.Member;
import com.ssafy.a705.location.domain.entity.Location;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryReaderImpl implements DiaryReader {

    private final DiaryRepository diaryRepository;

    @Override
    public Diary getDiary(Long diaryId) {
        Optional<Diary> diary = diaryRepository.findByIdAndDeletedAtIsNull(diaryId);
        if (diary.isEmpty()) {
            throw new DiaryNotFoundException();
        }
        return diary.get();
    }

    @Override
    public int countDiary(Location location, Member member) {
        return diaryRepository.countByLocationAndMember(location, member);
    }

    @Override
    public List<Diary> getAllDiaries(Member member, Integer locationCode,
            Long cursorId, int pageSize) {
        return diaryRepository.findDiariesWithThumbnailByMember(member, locationCode, cursorId,
                pageSize);
    }
}
