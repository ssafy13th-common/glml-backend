package com.ssafy.a705.domain.group._receipt.dto.response;

import com.ssafy.a705.domain.group._receipt.dto.OcrStatus;

public record FlaskOcrRes(OcrStatus ocrStatus, Object ocrResult) {

    public static FlaskOcrRes of(OcrStatus ocrStatus, Object ocrResult) {
        return new FlaskOcrRes(ocrStatus, ocrResult);
    }
}
