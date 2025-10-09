package com.ssafy.a705.group.presentation.receipt.dto.request;

public record FlaskOcrReq(String receiptPresignedUrl) {

    public static FlaskOcrReq of(String receiptPresignedUrl) {
        return new FlaskOcrReq(receiptPresignedUrl);
    }
}
