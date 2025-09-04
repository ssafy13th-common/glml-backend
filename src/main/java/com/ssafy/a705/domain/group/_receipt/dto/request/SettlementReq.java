package com.ssafy.a705.domain.group._receipt.dto.request;

import jakarta.validation.constraints.Min;
import java.util.List;

public record SettlementReq(
        @Min(0)
        Integer pricePerPerson,
        List<String> memberEmails) {

}
