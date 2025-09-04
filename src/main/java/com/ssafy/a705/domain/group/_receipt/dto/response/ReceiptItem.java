package com.ssafy.a705.domain.group._receipt.dto.response;

public record ReceiptItem(String name, Integer price) {

    public static ReceiptItem of(String name, Integer price) {
        return new ReceiptItem(name, price);
    }
}
