package com.ssafy.a705.domain.group._receipt.dto.response;

import java.util.List;

public record SettlementRes(List<SettlementInfoRes> settlements) {

    public static SettlementRes of(List<SettlementInfoRes> settlements) {
        return new SettlementRes(settlements);
    }

}
