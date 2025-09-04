package com.ssafy.a705.domain.group._receipt.dto.response;

import java.util.List;

public record OcrExecuteRes(List<ReceiptItem> receiptItems) {

    public static OcrExecuteRes of(List<ReceiptItem> receiptItems) {
        return new OcrExecuteRes(receiptItems);
    }
}
