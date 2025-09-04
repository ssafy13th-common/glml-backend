package com.ssafy.a705.domain.group._receipt.controller;

import com.ssafy.a705.domain.group._member.service.GroupMemberService;
import com.ssafy.a705.domain.group._receipt.dto.request.OcrExecuteReq;
import com.ssafy.a705.domain.group._receipt.dto.request.SettlementReq;
import com.ssafy.a705.domain.group._receipt.dto.response.OcrExecuteRes;
import com.ssafy.a705.domain.group._receipt.dto.response.SettlementRes;
import com.ssafy.a705.domain.group._receipt.service.OcrJobService;
import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups/{group-id}/receipts")
public class ReceiptRestController {

    private final GroupMemberService groupMemberService;
    private final OcrJobService ocrJobService;

    @PostMapping
    public ResponseEntity<ApiResponse<OcrExecuteRes>> execute(
            @RequestBody @Valid OcrExecuteReq ocrExecuteReq,
            @AuthenticationPrincipal
            CustomUserDetails userDetails) {
        OcrExecuteRes res = ocrJobService.execute(ocrExecuteReq, userDetails);
        return ApiResponse.ok(res);
    }

    @PutMapping("/settlements")
    public ResponseEntity<ApiResponse<SettlementRes>> settlement(
            @PathVariable("group-id") Long groupId,
            @RequestBody @Valid SettlementReq settlementReq,
            @AuthenticationPrincipal
            CustomUserDetails userDetails) {
        SettlementRes settlementRes = groupMemberService.updateGroupMembersFinalAmount(groupId,
                settlementReq, userDetails);
        return ApiResponse.ok(settlementRes);
    }

}