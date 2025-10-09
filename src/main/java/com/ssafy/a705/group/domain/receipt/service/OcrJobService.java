package com.ssafy.a705.group.domain.receipt.service;

import com.ssafy.a705.group.presentation.receipt.dto.request.OcrExecuteReq;
import com.ssafy.a705.group.presentation.receipt.dto.response.OcrExecuteRes;

public interface OcrJobService {
    OcrExecuteRes execute(OcrExecuteReq ocrExecuteReq);
}
