package com.ssafy.a705.domain.group._receipt.dto.response;

public record SettlementInfoRes(String memberEmail, Integer finalAmount) {

    public static SettlementInfoRes of(String memberEmail, Integer finalAmount) {
        return new SettlementInfoRes(memberEmail, finalAmount);
    }

}
