package com.ssafy.a705.group.presentation.receipt;

import com.ssafy.a705.global.common.controller.ApiResponse;
import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.application.participant.ParticipantApplicationService;
import com.ssafy.a705.group.application.receipt.ReceiptApplicationService;
import com.ssafy.a705.group.presentation.receipt.dto.request.OcrExecuteReq;
import com.ssafy.a705.group.presentation.receipt.dto.request.SettlementReq;
import com.ssafy.a705.group.presentation.receipt.dto.response.OcrExecuteRes;
import com.ssafy.a705.group.presentation.receipt.dto.response.SettlementRes;
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

    private final ParticipantApplicationService participantService;
    private final ReceiptApplicationService receiptService;

    @PostMapping
    public ResponseEntity<ApiResponse<OcrExecuteRes>> execute(
            @RequestBody @Valid OcrExecuteReq ocrExecuteReq,
            @AuthenticationPrincipal
            CustomUserDetails userDetails) {
        OcrExecuteRes res = receiptService.getOcrExecuteRes(ocrExecuteReq, userDetails);
        return ApiResponse.ok(res);
    }

    @PutMapping("/settlements")
    public ResponseEntity<ApiResponse<SettlementRes>> settlement(
            @PathVariable("group-id") Long groupId,
            @RequestBody @Valid SettlementReq settlementReq,
            @AuthenticationPrincipal
            CustomUserDetails userDetails) {
        SettlementRes settlementRes = participantService.updateParticipantsFinalAmount(groupId,
                settlementReq, userDetails);
        return ApiResponse.ok(settlementRes);
    }

}