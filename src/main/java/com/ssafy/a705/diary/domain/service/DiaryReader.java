package com.ssafy.a705.diary.domain.service;

import com.ssafy.a705.diary.domain.entity.Diary;
import com.ssafy.a705.member.domain.entity.Member;
import com.ssafy.a705.location.domain.entity.Location;
import java.util.List;

public interface DiaryReader {

    Diary getDiary(Long diaryId);

    int countDiary(Location location, Member member);

    List<Diary> getAllDiaries(Member member, Integer locationCode,
            Long cursorId, int pageSize);
}
