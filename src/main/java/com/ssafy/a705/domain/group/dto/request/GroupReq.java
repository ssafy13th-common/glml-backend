package com.ssafy.a705.domain.group.dto.request;

import com.ssafy.a705.domain.group.entity.GroupStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record GroupReq(
        @NotBlank(message = "그룹 이름이 입력되지 않았습니다.")
        String name,
        String summary,
        List<String> members
) {

}
