package com.ssafy.a705.group.application.receipt;

import com.ssafy.a705.global.security.login.dto.CustomUserDetails;
import com.ssafy.a705.group.infrastructure.receipt.service.OcrJobServiceImpl;
import com.ssafy.a705.group.presentation.receipt.dto.request.OcrExecuteReq;
import com.ssafy.a705.group.presentation.receipt.dto.response.OcrExecuteRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceiptApplicationService {

    private final OcrJobServiceImpl ocrJobService;

    public OcrExecuteRes getOcrExecuteRes(OcrExecuteReq ocrExecuteReq, CustomUserDetails userDetails) {
        return ocrJobService.execute(ocrExecuteReq);
    }
}
