package com.ssafy.a705.domain.diary.controller;

import com.ssafy.a705.domain.diary.dto.request.DiaryCreateReq;
import com.ssafy.a705.domain.diary.dto.request.DiaryUpdateReq;
import com.ssafy.a705.domain.diary.dto.response.DiaryCreateRes;
import com.ssafy.a705.domain.diary.dto.response.DiaryDetailRes;
import com.ssafy.a705.domain.diary.dto.response.DiaryInfosRes;
import com.ssafy.a705.domain.diary.service.DiaryService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/diaries")
public class DiaryRestController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<ApiResponse<DiaryCreateRes>> createDiary(
            @RequestBody @Valid DiaryCreateReq diaryCreateReq,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DiaryCreateRes res = diaryService.createDiary(diaryCreateReq, userDetails);
        return ApiResponse.create(res);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DiaryInfosRes>> getDiaries(
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false, value = "locationCode")
            Integer locationCode,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DiaryInfosRes res = diaryService.getDiaries(userDetails,
                locationCode, cursorId, size);
        return ApiResponse.ok(res);
    }

    @GetMapping("/{diary-id}")
    public ResponseEntity<ApiResponse<DiaryDetailRes>> getDiary(
            @PathVariable("diary-id") Long diaryId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        DiaryDetailRes res = diaryService.getDiary(diaryId, userDetails);
        return ApiResponse.ok(res);
    }

    @PutMapping("/{diary-id}")
    public ResponseEntity<ApiResponse<Void>> updateDiary(
            @PathVariable("diary-id") Long diaryId,
            @RequestBody @Valid DiaryUpdateReq diaryUpdateReq,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        diaryService.updateDiary(diaryId, diaryUpdateReq, userDetails);
        return ApiResponse.ok();
    }

    @DeleteMapping("/{diary-id}")
    public ResponseEntity<ApiResponse<Void>> deleteDiary(
            @PathVariable("diary-id") Long diaryId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        diaryService.deleteDiary(diaryId, userDetails);
        return ApiResponse.ok();
    }

}
