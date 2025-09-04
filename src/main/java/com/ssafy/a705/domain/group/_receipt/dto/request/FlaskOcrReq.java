package com.ssafy.a705.domain.group._receipt.dto.request;

public record FlaskOcrReq(String receiptPresignedUrl) {

    public static FlaskOcrReq of(String receiptPresignedUrl) {
        return new FlaskOcrReq(receiptPresignedUrl);
    }
}
