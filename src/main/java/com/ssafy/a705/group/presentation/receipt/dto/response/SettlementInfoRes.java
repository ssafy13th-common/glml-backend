package com.ssafy.a705.group.presentation.receipt.dto.response;

public record SettlementInfoRes(String memberEmail, Integer finalAmount) {

    public static SettlementInfoRes of(String memberEmail, Integer finalAmount) {
        return new SettlementInfoRes(memberEmail, finalAmount);
    }

}
