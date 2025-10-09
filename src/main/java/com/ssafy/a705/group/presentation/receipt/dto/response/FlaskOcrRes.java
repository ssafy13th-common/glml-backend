package com.ssafy.a705.group.presentation.receipt.dto.response;

import com.ssafy.a705.group.domain.receipt.entity.OcrStatus;

public record FlaskOcrRes(OcrStatus ocrStatus, Object ocrResult) {

    public static FlaskOcrRes of(OcrStatus ocrStatus, Object ocrResult) {
        return new FlaskOcrRes(ocrStatus, ocrResult);
    }
}
