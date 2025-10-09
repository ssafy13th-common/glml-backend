package com.ssafy.a705.group.presentation.group.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record GroupReq(
        @NotBlank(message = "그룹 이름이 입력되지 않았습니다.")
        String name,
        String summary,
        List<String> members
) {

}
